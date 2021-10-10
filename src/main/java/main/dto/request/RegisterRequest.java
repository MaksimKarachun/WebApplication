package main.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterRequest {

    @JsonProperty("e_mail")
    private String email;
    @Size(min = 6, message = "Пароль короче 6 символов.")
    private String password;
    @Pattern(regexp = "^[A-ZА-Я][a-zа-я]{1,25}$", message = "Неверноый формат имени пользователя.")
    private String name;
    private String captcha;
    @JsonProperty("captcha_secret")
    private String captchaSecret;
}
