package main.controller;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import main.exception.UploadImageException;
import main.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ImageController {

  private final ImageService imageService;

  @PostMapping("api/image")
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity<String> uploadImage(@RequestBody MultipartFile image)
      throws IOException, UploadImageException {
    return imageService.uploadImage(image);
  }
}
