package main;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.yml")
public class Main {

  @PostConstruct
  void started() {
    Calendar calendar = new GregorianCalendar();
    TimeZone timeZone = calendar.getTimeZone();
    TimeZone.setDefault(timeZone);
  }

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }
}


