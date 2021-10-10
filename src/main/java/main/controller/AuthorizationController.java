package main.controller;


import java.security.Principal;
import lombok.RequiredArgsConstructor;
import main.dto.request.LoginRequest;
import main.dto.request.RegisterRequest;
import main.dto.response.CaptchaResponse;
import main.dto.response.LoginResponse;
import main.dto.response.RegisterResponse;
import main.exception.LoginException;
import main.exception.DataBaseException;
import main.exception.RegisterValidationException;
import main.service.AuthorizationService;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @PostMapping(value = "/api/auth/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest)
        throws LoginException {
        return authorizationService.loginUser(loginRequest);
    }

    @GetMapping("/api/auth/check")
    public LoginResponse authCheckResponse(Principal principal){
        if (principal == null) {
            return new LoginResponse(false);
        }
        return authorizationService.authCheck(principal);
    }

    @GetMapping("/api/auth/captcha")
    public CaptchaResponse getCaptcha() throws DataBaseException {
        return authorizationService.getCaptchaCode();
    }

    @PostMapping(value = "/api/auth/register")
    public RegisterResponse registration(@Valid @RequestBody RegisterRequest registerRequest, Errors errors)
        throws DataBaseException, RegisterValidationException {
        if (errors.hasErrors()) {
            throw new RegisterValidationException(errors);
        }
        return authorizationService.registerNewUser(registerRequest);
    }
}
