package com.poc.emailconnector.routes;

import static org.apache.camel.LoggingLevel.INFO;

import jakarta.activation.FileDataSource;
import java.io.File;
import org.apache.camel.attachment.AttachmentMessage;
import org.apache.camel.attachment.DefaultAttachment;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderRoute extends RouteBuilder {
  @Value("${camel.component.mail.username}")
  private String username;

  @Value("${camel.component.mail.password}")
  private String password;

  @Override
  public void configure() throws Exception {
    from("seda:sendEmail")
        .log(INFO, "Sending Email")
        .process(
            exchange -> {
              File dir = new File("src/main/resources/attachments");
              File[] files = dir.listFiles();
              if (files != null) {
                for (File file : files) {
                  AttachmentMessage in = exchange.getIn(AttachmentMessage.class);
                  DefaultAttachment att =
                      new DefaultAttachment(new FileDataSource(file.getAbsoluteFile()));
                  att.addHeader("Content-Description", file.getName());
                  in.addAttachmentObject(file.getName(), att);
                }
              }
            })
        .toD(
            "smtp://smtp.gmail.com:587"
                + "?username="
                + username
                + "&password="
                + password
                + "&mail.smtp.auth=true"
                + "&mail.smtp.starttls.enable=true")
        .routeId("emailSender")
        .log(INFO, "Email Sent")
        .stop();
  }
}
