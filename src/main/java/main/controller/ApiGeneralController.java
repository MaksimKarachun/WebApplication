package main.controller;

import lombok.RequiredArgsConstructor;
import main.DTO.response.*;
import main.service.PostService;
import main.service.SettingsService;
import main.service.TagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingsService settingsService;
    private final TagService tagService;

    @GetMapping("/init")
    public InitResponse init(){
        return initResponse;
    }

    @GetMapping("/settings")
    public SettingsResponse settings(){
        return settingsService.getGlobalSettings();
    }

    @GetMapping("/auth/check")
    public AuthCheckResponse authCheckResponse(){
        return new AuthCheckResponse(false);
    }

    @GetMapping("/tag")
    public TagResponse tagResponse(){
        return tagService.getDefaultTagResponse();
    }
}
