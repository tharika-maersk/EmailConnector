package com.poc.EmailConnector.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailToAMQRoute extends RouteBuilder {

  @Value("${camel.component.mail.username}")
  private String username;

  @Value("${camel.component.mail.password}")
  private String password;

  @Override
  public void configure() throws Exception {
    from("imaps://imap.gmail.com:993"
            + "?username=" + username
            + "&password=" + password
            + "&mail.imap.auth=true"
            + "&mail.imap.starttls.enable=true"
            + "&delete=false&unseen=true&delay=60000")
            .routeId("emailReceiver")
            .log(LoggingLevel.INFO, "ROUTE STARTED: Listening to Gmail IMAP")
            .log(LoggingLevel.INFO, "Message headers: ${headers}")
            .log(LoggingLevel.INFO, "EMAIL NOTIFICATION RECEIVED : ${body}")
            .stop();
  }
}
