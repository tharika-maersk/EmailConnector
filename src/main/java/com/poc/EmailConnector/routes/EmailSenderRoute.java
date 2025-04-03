package com.poc.EmailConnector.routes;

import org.apache.camel.LoggingLevel;
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
        .log(LoggingLevel.INFO, "SENDING EMAIL NOTIFICATION")
        .toD(
            "smtp://smtp.gmail.com:587"
                + "?username=" + username
                + "&password=" + password
                + "&mail.smtp.auth=true"
                + "&mail.smtp.starttls.enable=true")
            .routeId("emailSender")
        .log(LoggingLevel.INFO, "EMAIL NOTIFICATION SENT")
        .stop();
  }
}
