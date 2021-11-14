package main.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import lombok.RequiredArgsConstructor;
import main.dto.request.LoginRequest;
import main.dto.response.*;
import main.dto.request.RegisterRequest;
import main.repository.PostRepository;
import main.stringConst.StringConstant;
import main.exception.LoginException;
import main.exception.DataBaseException;
import main.model.CaptchaCode;
import main.model.User;
import main.repository.CaptchaRepository;
import main.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

  private final CaptchaRepository captchaRepository;

  private final StringUtilsService stringUtilsService;

  private final UserRepository userRepository;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  private final AuthenticationManager authenticationManager;

  private final PostRepository postRepository;


  public LoginResponse loginUser(LoginRequest loginRequest) throws LoginException {
    try {
      Authentication authentication = authenticationManager
          .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
              loginRequest.getPassword()));
      SecurityContextHolder.getContext().setAuthentication(authentication);
      org.springframework.security.core.userdetails.User userPrincipal =
          (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
      return prepareLoginResponse(userPrincipal.getUsername());
    } catch (Exception e) {
      // TODO: 10.10.2021 Добавить запись в логи
    }
    throw new LoginException();
  }

  public LogoutResponse logoutUser(HttpServletRequest httpServletRequest) {
    HttpSession session = httpServletRequest.getSession(false);
    if (session != null) {
      session.invalidate();
    }
    return new LogoutResponse(true);
  }

  public LoginResponse authCheck(Principal userPrincipal) {
    return prepareLoginResponse(userPrincipal.getName());
  }

  public CaptchaResponse getCaptchaCode() {
    captchaRepository.deleteOldCaptcha();
    Cage cage = new GCage();
    String code = stringUtilsService.getRandomString(5);
    byte[] imageByte = cage.draw(code);
    String encodedString = Base64.getEncoder().encodeToString(imageByte);
    String resultEncodeStr = "data:image/png;base64, " + encodedString;
    String secret = stringUtilsService.getRandomString(15);
    addCaptchaToDB(code, secret);
    return prepareCaptchaResponse(resultEncodeStr, secret);
  }

  public ResponseEntity<RegisterResponse> registerNewUser(RegisterRequest registerRequest) {
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
    if (userRepository.findByEmail(userEmail) != null) {
      registerResponse.setResult(false);
      error.setEmail(StringConstant.USER_ALREADY_REGISTER_ERROR_MESSAGE);
    }
    //добавление нового пользователя в БД при отсутствии ошибок
    if (registerResponse.getResult()) {
      addUserToDB(userEmail, name, registerRequest.getPassword());
      return new ResponseEntity<>(registerResponse, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(registerResponse, HttpStatus.BAD_REQUEST);
    }
  }

  private CaptchaResponse prepareCaptchaResponse(String encodedString, String secret) {
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
    newUser.setPassword(bCryptPasswordEncoder.encode(password));
    userRepository.save(newUser);
    if (userRepository.findById(newUser.getId()).isEmpty()) {
      throw new DataBaseException(StringConstant.DATA_BASE_ERROR_MESSAGE);
    }
  }

  private LoginResponse prepareLoginResponse(String userEmail) {
    User user = userRepository.findByEmail(userEmail);
    LoginUserDTO loginUserDTO = new LoginUserDTO();
    loginUserDTO.setId(user.getId());
    loginUserDTO.setName(user.getName());
    loginUserDTO.setPhoto(user.getPhoto());
    loginUserDTO.setModeration(user.isModerator());
    loginUserDTO.setModerationCount(getModerationCount(user));
    loginUserDTO.setEmail(user.getEmail());
    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setResult(true);
    loginResponse.setUser(loginUserDTO);
    return loginResponse;
  }

  private int getModerationCount(User user) {
    if (!user.isModerator()) {
      return 0;
    }
    return postRepository.getCountModerationPosts();
  }
}
