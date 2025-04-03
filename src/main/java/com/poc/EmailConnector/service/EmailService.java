package com.poc.EmailConnector.service;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {
    private final ProducerTemplate producerTemplate;

    public EmailService(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    @Value("${camel.component.mail.username}")
    private String from;

    public void sendEmail(String subject, String body) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("From", from);
        headers.put("Subject", subject);
        producerTemplate.sendBodyAndHeaders("seda:sendEmail", body, headers);
    }
}
