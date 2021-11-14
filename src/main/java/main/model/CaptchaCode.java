package main.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "captcha_codes")
@Data
public class CaptchaCode {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private int id;

  @DateTimeFormat(pattern = "yyyy.MM.dd HH-mm")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH-mm")
  @Column(columnDefinition = "DATETIME", nullable = false)
  private Date time;

  @Column(columnDefinition = "TINYTEXT", nullable = false)
  private String code;

  @Column(name = "secret_code", columnDefinition = "TINYTEXT", nullable = false)
  private String secretCode;
}
