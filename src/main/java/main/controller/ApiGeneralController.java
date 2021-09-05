package main.controller;

import lombok.RequiredArgsConstructor;
import main.dto.response.*;
import main.service.AuthorizationService;
import main.service.SettingsService;
import main.service.TagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingsService settingsService;
    private final TagService tagService;
    private final AuthorizationService authorizationService;

    @GetMapping("/api/init")
    public InitResponse init(){
        return initResponse;
    }

    @GetMapping("/api/settings")
    public SettingsResponse settings(){
        return settingsService.getGlobalSettings();
    }

    @GetMapping("/api/auth/check")
    public AuthCheckResponse authCheckResponse(){
        return new AuthCheckResponse(false);
    }

    @GetMapping("/api/tag")
    public TagResponse tagResponse(){
        return tagService.getDefaultTagResponse();
    }
}
