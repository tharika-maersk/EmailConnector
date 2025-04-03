package com.poc.emailconnector.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailContentProcessor implements Processor {
  private static final Logger log = LoggerFactory.getLogger(EmailContentProcessor.class);

  @Override
  public void process(Exchange exchange) throws Exception {
    String message = exchange.getMessage().getBody(String.class);
    log.info("Message to be processed is : {}", message);
  }
}
