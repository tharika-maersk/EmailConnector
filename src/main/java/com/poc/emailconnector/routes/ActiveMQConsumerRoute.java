package com.poc.emailconnector.routes;

import static org.apache.camel.LoggingLevel.INFO;

import com.poc.emailconnector.processor.EmailPayload;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ActiveMQConsumerRoute extends RouteBuilder {
  private static final Logger logger = LoggerFactory.getLogger(ActiveMQConsumerRoute.class);

  @Override
  public void configure() throws Exception {
    from("jms:queue:TEST.QUEUE.1")
        .routeId("ActiveMQConsumer")
        .process(
            exchange -> {
              EmailPayload payload = exchange.getIn().getBody(EmailPayload.class);

              logger.info("Subject: {}", payload.getSubject());
              logger.info("Body: {}", payload.getBody());
              logger.info("Filenames: {}", payload.getFilenames());

              List<byte[]> attachments = payload.getAttachments();
              List<String> filenames = payload.getFilenames();
              List<Map<String, Object>> attachmentsInfo = new ArrayList<>();
              for (int i = 0; i < attachments.size(); i++) {
                logger.info(
                    "File {} , File size : {} bytes", filenames.get(i), attachments.get(i).length);
                Map<String, Object> attachment = new HashMap<>();
                attachment.put("fileName", filenames.get(i));
                attachment.put("fileContent", attachments.get(i));
                attachmentsInfo.add(attachment);
              }
              exchange.getIn().setBody(attachmentsInfo);
            })
        .split(body())
        .parallelProcessing()
        .setHeader("CamelFileName", simple("${body[fileName]}"))
        .setBody(simple("${body[fileContent]}"))
        .to("file:src/main/resources/attachments-output")
        .log(INFO, "Added ${file:name} to output directory");
  }
}
