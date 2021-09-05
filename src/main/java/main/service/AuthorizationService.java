package main.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import lombok.RequiredArgsConstructor;
import main.dto.responseDto.RegisterErrorDTO;
import main.dto.request.RegisterRequest;
import main.dto.response.CaptchaResponse;
import main.dto.response.RegisterResponse;
import main.StringConst.StringConstant;
import main.exception.DataBaseException;
import main.model.CaptchaCode;
import main.model.User;
import main.repository.CaptchaRepository;
import main.repository.UsersRepository;
import org.springframework.stereotype.Service;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final CaptchaRepository captchaRepository;

    private final StringUtilsService stringUtilsService;

    private final UsersRepository usersRepository;

    public CaptchaResponse getCaptchaCode() {
        captchaRepository.deleteOldCaptcha();
        Cage cage = new GCage();
        String code = stringUtilsService.getRandomString(5);
        byte[] imageByte = cage.draw(code);
        String encodedString  = Base64.getEncoder().encodeToString(imageByte);
        String resultEncodeStr = "data:image/png;base64, " + encodedString;
        String secret = stringUtilsService.getRandomString(15);
        addCaptchaToDB(code, secret);
        return prepareCaptchaResponse(resultEncodeStr, secret);
    }

    public RegisterResponse registerNewUser(RegisterRequest registerRequest) {
        RegisterResponse registerResponse = new RegisterResponse();
        RegisterErrorDTO error = new RegisterErrorDTO();
        registerResponse.setResult(true);
        registerResponse.setErrors(error);
        String userEmail = registerRequest.getEmail();
        String requestSecret = registerRequest.getCaptchaSecret();
        String name = registerRequest.getName();
        //проверка капчи
        CaptchaCode captcha = captchaRepository.findBySecretCode(requestSecret);
        if (captcha == null || !captcha.getCode().equals(registerRequest.getCaptcha())) {
            registerResponse.setResult(false);
            error.setCaptcha(StringConstant.CAPTCHA_CODE_ERROR_MESSAGE);
        }
        //проверка повторной регистрации
        if (usersRepository.findByEmail(userEmail) != null) {
            registerResponse.setResult(false);
            error.setEmail(StringConstant.USER_ALREADY_REGISTER_ERROR_MESSAGE);
        }
        //проверка имени пользователя
        if (!stringUtilsService.checkRegisterName(name)) {
            registerResponse.setResult(false);
            error.setName(StringConstant.USER_NAME_ERROR_MESSAGE);
        }
        //добавление нового польхователя в БД при отсутсвии ошибок
        if (registerResponse.getResult()) {
            addUserToDB(userEmail, name, registerRequest.getPassword());
        }
        return registerResponse;
    }

    private CaptchaResponse prepareCaptchaResponse(String encodedString, String secret){
        CaptchaResponse captchaResponse = new CaptchaResponse();
        captchaResponse.setSecret(secret);
        captchaResponse.setImage(encodedString);
        return captchaResponse;
    }

    private void addCaptchaToDB(String code, String secret) throws DataBaseException {
        CaptchaCode captchaCode = new CaptchaCode();
        captchaCode.setCode(code);
        captchaCode.setSecretCode(secret);
        captchaCode.setTime(new Date());
        captchaRepository.save(captchaCode);
        if (captchaRepository.findById(captchaCode.getId()).isEmpty()) {
            throw new DataBaseException(StringConstant.DATA_BASE_ERROR_MESSAGE);
        }
    }

    private void addUserToDB(String email, String name, String password) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setModerator(false);
        newUser.setRegTime(new Date());
        newUser.setName(name);
        newUser.setPassword(password);
        usersRepository.save(newUser);
        if (usersRepository.findById(newUser.getId()).isEmpty()) {
            throw new DataBaseException(StringConstant.DATA_BASE_ERROR_MESSAGE);
        }
    }
}
