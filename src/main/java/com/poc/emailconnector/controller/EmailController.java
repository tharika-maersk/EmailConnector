package com.poc.emailconnector.controller;

import com.poc.emailconnector.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EmailController {
  private final EmailService emailService;

  @Autowired
  public EmailController(EmailService emailService) {
    this.emailService = emailService;
  }

  @PostMapping("/sendEmail")
  public String sendEmail(
      @RequestParam(name = "toEmail", required = false, defaultValue = "tharika.maersk@gmail.com")
          String toEmail) {
    emailService.sendEmail(toEmail, "Testing Email Service", "This is a test email.");
    return "Email sent!";
  }
}
