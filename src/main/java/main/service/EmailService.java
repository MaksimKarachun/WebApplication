package main.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  private final JavaMailSender javaMailSender;
  @Value("${spring.mail.username}")
  private String username;

  public EmailService(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
  }

  public void sendMail(String subject, String messageBody, String email) {
    MimeMessage message = javaMailSender.createMimeMessage();
    try {
      MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
      messageHelper.setFrom(username);
      messageHelper.setTo(email);
      messageHelper.setSubject(subject);
      message.setContent(messageBody, "text/html; charset=UTF-8");
      javaMailSender.send(message);
    } catch (MessagingException e) {
      throw new RuntimeException(String.format("Не удалось отправить email на почту: %s", email));
    }
  }

}
