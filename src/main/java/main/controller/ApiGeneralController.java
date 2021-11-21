package main.controller;

import java.io.IOException;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import main.dto.request.AddPostCommentRequest;
import main.dto.request.EditProfileRequest;
import main.dto.request.ModerationPostRequest;
import main.dto.response.*;
import main.exception.EditProfileException;
import main.exception.UploadImageException;
import main.service.ModerationService;
import main.service.PostService;
import main.service.ProfileService;
import main.service.SettingsService;
import main.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ApiGeneralController {

  private final InitResponse initResponse;
  private final SettingsService settingsService;
  private final TagService tagService;
  private final ModerationService moderationService;
  private final PostService postService;


  @GetMapping("/api/init")
  public InitResponse init() {
    return initResponse;
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

  @PostMapping("/api/comment")
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity<AddPostCommentResponse> addPostComment(
      @RequestBody AddPostCommentRequest request, Principal principal) {
    return postService.addPostComment(request, principal.getName());
  }
}
