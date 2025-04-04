package com.poc.emailconnector.routes;

import static org.apache.camel.LoggingLevel.INFO;

import com.poc.emailconnector.processor.EmailContentProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailToActiveMQRoute extends RouteBuilder {

  @Value("${camel.component.mail.username}")
  private String username;

  @Value("${camel.component.mail.password}")
  private String password;

  @Override
  public void configure() throws Exception {
    from("imaps://imap.gmail.com:993"
            + "?username="
            + username
            + "&password="
            + password
            + "&mail.imap.auth=true"
            + "&mail.imap.starttls.enable=true"
            + "&delete=false&unseen=true")
        .routeId("emailReceiver")
        .log(INFO, "Email received;\n body : ${body} subject : ${headers.Subject}")
        .process(new EmailContentProcessor())
        .marshal()
        .json(JsonLibrary.Jackson)
        .to("jms:queue:TEST.QUEUE.1")
        .log("Message sent to Active MQ");
  }
}
