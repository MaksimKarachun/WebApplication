package main.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "captcha_codes")
public class CaptchaCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @NotNull
    private Date time;

    @Column(columnDefinition = "TINYTEXT")
    @NotNull
    private String code;

    @Column(name = "secret_code", columnDefinition = "TINYTEXT")
    @NotNull
    private String secretCode;
}
