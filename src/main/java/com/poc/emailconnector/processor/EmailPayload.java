package com.poc.emailconnector.processor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmailPayload implements Serializable {
  private String subject;
  private String body;
  private List<String> filenames;
  private List<byte[]> attachments;

  public EmailPayload(
      String subject, String body, List<String> filenames, List<byte[]> attachments) {
    this.subject = subject;
    this.body = body;
    this.filenames = filenames;
    this.attachments = attachments;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public List<String> getFilenames() {
    return filenames;
  }

  public List<byte[]> getAttachments() {
    return attachments;
  }

  public void setFilenames(List<String> filenames) {
    this.filenames = filenames;
  }

  public void setAttachments(List<byte[]> attachments) {
    this.attachments = attachments;
  }

  public Map<String, Serializable> toMap() {
    Map<String, Serializable> map = new HashMap<>();
    map.put("subject", this.subject);
    map.put("body", this.body);
    map.put("filenames", (Serializable) this.filenames);
    map.put("attachments", (Serializable) this.attachments);
    return map;
  }
}
