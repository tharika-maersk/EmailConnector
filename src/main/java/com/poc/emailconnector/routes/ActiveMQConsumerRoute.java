package com.poc.emailconnector.routes;

import static org.apache.camel.LoggingLevel.INFO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ActiveMQConsumerRoute extends RouteBuilder {
  private static final Logger logger = LoggerFactory.getLogger(ActiveMQConsumerRoute.class);

  @Override
  @SuppressWarnings("unchecked")
  public void configure() throws Exception {
    from("jms:queue:TEST.QUEUE.1")
        .routeId("ActiveMQConsumer")
        .unmarshal()
        .json(JsonLibrary.Jackson)
        .process(
            exchange -> {
              Map<String, Object> payload = exchange.getIn().getBody(Map.class);

              List<String> filenames = (List<String>) payload.get("filenames");
              List<String> attachments = (List<String>) payload.get("attachments");

              List<Map<String, Object>> attachmentsInfo = new ArrayList<>();
              for (int i = 0; i < filenames.size(); i++) {
                Map<String, Object> attachment = new HashMap<>();
                attachment.put("fileName", filenames.get(i));
                attachment.put(
                    "fileContent", java.util.Base64.getDecoder().decode(attachments.get(i)));
                attachmentsInfo.add(attachment);
              }
              exchange.getIn().setBody(attachmentsInfo);
            })
        .split(body())
        .parallelProcessing()
        .setHeader("CamelFileName", simple("${body[fileName]}"))
        .setBody(simple("${body[fileContent]}"))
        .to("file:src/main/resources/attachments-output")
        .log(INFO, "Added Attachments to output directory");
  }
}
