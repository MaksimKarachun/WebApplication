package main.controller;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import main.dto.request.ModerationPostRequest;
import main.dto.response.*;
import main.service.AuthorizationService;
import main.service.ModerationService;
import main.service.SettingsService;
import main.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ApiGeneralController {

  private final InitResponse initResponse;
  private final SettingsService settingsService;
  private final TagService tagService;
  private final ModerationService moderationService;

  @GetMapping("/api/init")
  public InitResponse init() {
    return initResponse;
  }

  @GetMapping("/api/settings")
  public SettingsResponse settings() {
    return settingsService.getGlobalSettings();
  }

  @GetMapping("/api/tag")
  public TagResponse tagResponse() {
    return tagService.getDefaultTagResponse();
  }

  @PostMapping("/api/moderation")
  @PreAuthorize("hasAuthority('user:moderate')")
  public ResponseEntity<ModerationPostResponse> addPost(
      @RequestBody ModerationPostRequest request, Principal principal) {
    return moderationService.moderatePost(request, principal.getName());
  }
}
