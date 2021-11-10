package main.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import main.dto.request.EditProfileRequest;
import main.dto.response.EditProfileResponse;
import main.exception.EditProfileException;
import main.model.User;
import main.repository.UserRepository;
import main.stringConst.StringConstant;
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

  public ResponseEntity<EditProfileResponse> editProfile(MultipartFile photo, String name,
      String email, boolean removePhoto) {

    return null;
  }

  public ResponseEntity<EditProfileResponse> editProfile(EditProfileRequest request, String userEmail)
      throws EditProfileException {
    User user = Optional.of(userRepository.findByEmail(userEmail))
        .orElseThrow(RuntimeException::new);
    if (request.getName() != null) {
      user.setName(request.getName());
    }
    if (request.getPassword() != null) {
      user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
    }
    if (!request.getEmail().equals(userEmail)) {
      if (userRepository.findByEmail(request.getEmail()) == null) {
        user.setEmail(request.getEmail());
      } else {
        throw new EditProfileException(StringConstant.USER_WITH_EMAIL_ALREADY_EXIST);
      }
    }
    if (request.isRemovePhoto()) {
      user.setPhoto("");
    }
    userRepository.save(user);
    EditProfileResponse response = new EditProfileResponse();
    response.setResult(true);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
