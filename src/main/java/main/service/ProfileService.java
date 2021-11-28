package main.service;

import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import main.dto.request.EditProfileRequest;
import main.dto.response.EditProfileResponse;
import main.exception.EditProfileException;
import main.exception.UploadImageException;
import main.model.User;
import main.repository.UserRepository;
import main.config.StringConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProfileService {

  private final UserRepository userRepository;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  private final ImageService imageService;

  public ResponseEntity<EditProfileResponse> editProfile(MultipartFile photo, String name,
      String email, String password, boolean removePhoto, String userEmail)
      throws IOException, UploadImageException, EditProfileException {
    User user = userRepository.findByEmail(userEmail);
    String imagePath = imageService.uploadProfileImage(photo);
    commonEditProfile(user, name, email, password, removePhoto, imagePath);
    return new ResponseEntity<>(new EditProfileResponse(true), HttpStatus.OK);
  }

  public ResponseEntity<EditProfileResponse> editProfile(EditProfileRequest request,
      String userEmail)
      throws EditProfileException {
    User user = Optional.of(userRepository.findByEmail(userEmail))
        .orElseThrow(RuntimeException::new);
    commonEditProfile(user, request.getName(), request.getEmail(), request.getPassword(),
        request.isRemovePhoto(), null);
    EditProfileResponse response = new EditProfileResponse();
    response.setResult(true);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  private void commonEditProfile(User user, String name, String email, String password,
      boolean removePhoto, String imagePath)
      throws EditProfileException {
    if (name != null) {
      user.setName(name);
    }
    if (password != null) {
      user.setPassword(bCryptPasswordEncoder.encode(password));
    }
    if (!email.equals(user.getEmail())) {
      if (userRepository.findByEmail(email) == null) {
        user.setEmail(email);
      } else {
        throw new EditProfileException(StringConstant.USER_WITH_EMAIL_ALREADY_EXIST);
      }
    }
    if (removePhoto) {
      user.setPhoto("");
    }
    if (imagePath != null) {
      user.setPhoto(imagePath);
    }
    userRepository.save(user);
  }
}
