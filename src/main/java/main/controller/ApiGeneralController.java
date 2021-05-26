package main.controller;

import main.api.response.*;
import main.service.PostService;
import main.service.SettingsService;
import main.service.TagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final AuthCheckResponse authCheckResponse;
    private final SettingsService settingsService;
    private final PostService postService;
    private final TagService tagService;

    public ApiGeneralController(InitResponse initResponse, AuthCheckResponse authCheckResponse, SettingsService settingsService, PostService postService, TagService tagService) {
        this.initResponse = initResponse;
        this.authCheckResponse = authCheckResponse;
        this.settingsService = settingsService;
        this.postService = postService;
        this.tagService = tagService;
    }

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
        return authCheckResponse;
    }

    @GetMapping("/post")
    public PostResponse postResponse(@RequestParam int offset, @RequestParam int limit, @RequestParam String mode){
        return postService.getPosts(offset, limit, mode);
    }

    @GetMapping("/tag")
    public TagResponse tagResponse(){
        return tagService.getDefaultTagResponse();
    }
}
