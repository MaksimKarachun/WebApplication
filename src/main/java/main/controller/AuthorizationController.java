package main.controller;


import lombok.RequiredArgsConstructor;
import main.dto.request.RegisterRequest;
import main.dto.response.CaptchaResponse;
import main.dto.response.RegisterResponse;
import main.StringConst.StringConstant;
import main.exception.DataBaseException;
import main.service.AuthorizationService;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.ValidationException;

@RestController
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @GetMapping("/api/auth/captcha")
    public CaptchaResponse getCaptcha() throws DataBaseException {
        return authorizationService.getCaptchaCode();
    }

    @PostMapping(value = "/api/auth/register",
            consumes = "application/json",
            produces = "application/json")
    public RegisterResponse registration(@Valid @RequestBody RegisterRequest registerRequest, Errors errors)
            throws DataBaseException {
        if (errors.hasErrors()) {
            throw new ValidationException(StringConstant.VALIDATION_ERROR_MESSAGE);
        }
        return authorizationService.registerNewUser(registerRequest);
    }
}
