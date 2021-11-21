package main.controller;

import java.io.IOException;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import main.dto.request.EditProfileRequest;
import main.dto.response.EditProfileResponse;
import main.exception.EditProfileException;
import main.exception.UploadImageException;
import main.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class ProfileController {

  private final ProfileService profileService;


  @PostMapping(value = "/my", consumes = {"application/json"})
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity<EditProfileResponse> editProfile(@RequestBody EditProfileRequest request,
      Principal principal) throws EditProfileException {
    return profileService.editProfile(request, principal.getName());
  }

  @PostMapping(value = "/my", consumes = {"multipart/form-data"})
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity<EditProfileResponse> editProfile(@RequestParam MultipartFile photo,
                                                        @RequestParam String name,
                                                        @RequestParam String email,
                                                        @RequestParam(required = false) String password,
                                                        @RequestParam boolean removePhoto,
                                                        Principal principal)
      throws IOException, UploadImageException, EditProfileException {
    return profileService.editProfile(photo, name, email, password, removePhoto, principal.getName());
  }
}
