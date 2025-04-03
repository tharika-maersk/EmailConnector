package com.poc.EmailConnector.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class EmailContentProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        String message = exchange.getMessage().getBody(String.class);
        System.out.println("Message is " + message);
    }
}
