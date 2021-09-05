package main.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @JsonProperty("e_mail")
    String email;
    String password;
    String name;
    String captcha;
    @JsonProperty("captcha_secret")
    String captchaSecret;
}
