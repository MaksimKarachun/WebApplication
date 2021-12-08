package main.template;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailTemplate {

  @Value("${spring.mail.address}")
  private String mailAddress;

  @Bean(name = "passRecoveryTemplateMessage")
  public String getPassRecoveryMessage(){
    return "Hello, to recovery your password follow to link " +
        "<a href=\"" + mailAddress + "/login/change-password/%s\">Password recovery</a>";
  }
}
