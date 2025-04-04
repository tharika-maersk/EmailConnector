package com.poc.emailconnector.routes;

import com.poc.emailconnector.processor.EmailPayload;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
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

              File outputDir = new File("src/main/resources/attachments-output");
              for (int i = 0; i < attachments.size(); i++) {
                byte[] fileBytes = attachments.get(i);
                String fileName = filenames.get(i);
                File file = new File(outputDir, fileName);
                logger.info(
                    "Attachment {} , attachment size : {} bytes", (i + 1), fileBytes.length);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                  fos.write(fileBytes);
                }
              }
            });
  }
}
