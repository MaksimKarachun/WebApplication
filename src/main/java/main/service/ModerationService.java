package main.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import main.dto.request.ModerationPostRequest;
import main.dto.response.ModerationPostResponse;
import main.exception.ModerationPostException;
import main.exception.PostNotFoundException;
import main.model.Post;
import main.model.User;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import static main.projectEnum.ModerateDecision.DECLINE;
import static main.projectEnum.ModerateDecision.ACCEPT;
import static main.model.ModerationStatus.DECLINED;
import static main.model.ModerationStatus.ACCEPTED;

/**
 * Модерация постов.
 */
@Service
@RequiredArgsConstructor
public class ModerationService {

  private final PostRepository postRepository;

  private final UserRepository userRepository;

  public ResponseEntity<ModerationPostResponse> moderatePost(ModerationPostRequest request,
      String moderatorEmail) {
    ModerationPostResponse response = new ModerationPostResponse();
    User moderator = Optional.of(userRepository.findByEmail(moderatorEmail)).orElseThrow(
        ModerationPostException::new);
    Post post = Optional.of(postRepository.getPostById(request.getPostId()))
        .orElseThrow(ModerationPostException::new);
    if (!request.getDecision().equals(DECLINE.getName()) && !request.getDecision()
        .equals(ACCEPT.getName())) {
      throw new ModerationPostException();
    }
    if (request.getDecision().equals(DECLINE.getName())) {
      post.setModerationStatus(DECLINED);
    }
    if (request.getDecision().equals(ACCEPT.getName())) {
      post.setModerationStatus(ACCEPTED);
    }
    post.setModeratorId(moderator);
    postRepository.save(post);
    response.setResult(true);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
