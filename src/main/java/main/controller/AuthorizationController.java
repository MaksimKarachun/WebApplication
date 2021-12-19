package main.controller;


import java.security.Principal;
import lombok.RequiredArgsConstructor;
import main.dto.request.LoginRequest;
import main.dto.request.PasswordChangeRequest;
import main.dto.request.RegisterRequest;
import main.dto.request.RestoreRequest;
import main.dto.response.CaptchaResponse;
import main.dto.response.LoginResponse;
import main.dto.response.PasswordChangeResponse;
import main.dto.response.RegisterResponse;
import main.dto.response.RestoreResponse;
import main.exception.LoginException;
import main.exception.DataBaseException;
import main.exception.RegisterValidationException;
import main.service.AuthorizationService;
import main.service.SettingsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthorizationController {

  private final AuthorizationService authorizationService;

  private final SettingsService settingsService;

  @PostMapping(value = "/login")
  public LoginResponse login(@RequestBody LoginRequest loginRequest)
      throws LoginException {
    return authorizationService.loginUser(loginRequest);
  }

  @GetMapping(value = "/logout")
  public ResponseEntity<LoginResponse> logout() {
    SecurityContextHolder.clearContext();
    return new ResponseEntity<>(new LoginResponse(true), HttpStatus.OK);
  }

  @GetMapping("/check")
  public LoginResponse authCheckResponse(Principal principal) {
    if (principal == null) {
      return new LoginResponse(false);
    }
    return authorizationService.authCheck(principal);
  }

  @GetMapping("/captcha")
  public CaptchaResponse getCaptcha() throws DataBaseException {
    return authorizationService.getCaptchaCode();
  }

  @PostMapping(value = "/register")
  public ResponseEntity<RegisterResponse> registration(
      @Valid @RequestBody RegisterRequest registerRequest, Errors errors)
      throws DataBaseException, RegisterValidationException {
    if (errors.hasErrors()) {
      throw new RegisterValidationException(errors);
    }
    if (!settingsService.getMultiuserModeSetting()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return authorizationService.registerNewUser(registerRequest);
  }

  @PostMapping(value = "/restore")
  public ResponseEntity<RestoreResponse> restore(@RequestBody RestoreRequest request){
    return authorizationService.restorePassword(request);
  }

  @PostMapping(value = "/password")
  public ResponseEntity<PasswordChangeResponse> restore(@RequestBody PasswordChangeRequest request){
    return authorizationService.changePassword(request);
  }
}
