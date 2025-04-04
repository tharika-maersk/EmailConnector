package com.poc.emailconnector.routes;

import java.io.File;
import java.io.FileOutputStream;
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

              File outputDir = new File("src/main/resources/attachments-output");
              for (int i = 0; i < filenames.size(); i++) {
                String base64 = attachments.get(i);
                byte[] fileBytes = java.util.Base64.getDecoder().decode(base64);
                String fileName = filenames.get(i);
                File file = new File(outputDir, fileName);
                logger.info(
                    "Attachment {} , attachment size : {} bytes", (i + 1), fileBytes.length);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                  fos.write(fileBytes);
                }
              }
              logger.info("Attachments Added to output dir");
            });
  }
}
