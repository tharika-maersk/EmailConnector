package com.poc.emailconnector.processor;

import jakarta.activation.DataHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.attachment.AttachmentMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailContentProcessor implements Processor {
  private static final Logger log = LoggerFactory.getLogger(EmailContentProcessor.class);

  @Override
  public void process(Exchange exchange) throws Exception {
    try {
      String body = exchange.getIn().getBody(String.class);
      String subject = exchange.getIn().getHeader("Subject", String.class);

      List<byte[]> attachmentsInBinary = new ArrayList<>();
      List<String> attachmentsFileNames = new ArrayList<>();

      AttachmentMessage attachmentMessage = exchange.getMessage(AttachmentMessage.class);
      Map<String, DataHandler> attachments = attachmentMessage.getAttachments();
      if (!attachments.isEmpty()) {
        for (String name : attachments.keySet()) {
          DataHandler dh = attachments.get(name);
          String filename = dh.getName();
          byte[] att =
              exchange.getContext().getTypeConverter().convertTo(byte[].class, dh.getInputStream());
          attachmentsFileNames.add(filename);
          attachmentsInBinary.add(att);
        }
      }
      log.info("attached files {}", attachmentsFileNames);
      EmailPayload payload =
          new EmailPayload(subject, body, attachmentsFileNames, attachmentsInBinary);
      exchange.getIn().setBody(payload);
    } catch (Exception e) {
      log.error("Exception Occurred while Processing the Mail : {}", e.getMessage());
    }
  }
}
