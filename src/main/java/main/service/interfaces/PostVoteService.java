package main.service.interfaces;

import main.dto.request.PostVoteRequest;
import main.dto.response.PostVoteResponse;
import main.projectEnum.Vote;
import org.springframework.http.ResponseEntity;

public interface PostVoteService {

  /**
   * Лайк/дизлайк поста
   */
  ResponseEntity<PostVoteResponse> votePost(PostVoteRequest request, String userEmail, Vote vote);
}
