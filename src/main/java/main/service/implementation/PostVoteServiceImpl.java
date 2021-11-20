package main.service.implementation;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import main.dto.request.PostVoteRequest;
import main.dto.response.PostVoteResponse;
import main.exception.PostVoteException;
import main.model.Post;
import main.model.PostVote;
import main.model.User;
import main.projectEnum.Vote;
import main.repository.PostRepository;
import main.repository.UserRepository;
import main.service.interfaces.PostVoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostVoteServiceImpl implements PostVoteService {

  private final UserRepository userRepository;

  private final PostRepository postRepository;

  @Override
  public ResponseEntity<PostVoteResponse> votePost(PostVoteRequest request, String userEmail,
      Vote vote) throws PostVoteException {
    User user = Optional.of(userRepository.findByEmail(userEmail))
        .orElseThrow(PostVoteException::new);
    Post post = postRepository.getPostById(request.getPostId());
    List<PostVote> postVoteList = post.getPostVotes();
    if (post.getUser().getId() != user.getId() && checkDoubleVote(postVoteList, user,
        vote.getValue())) {
      addPostVote(user, post, vote.getValue());
      return new ResponseEntity<>(new PostVoteResponse(true), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(new PostVoteResponse(false), HttpStatus.OK);
    }
  }

  private void addPostVote(User user, Post post, int value) {
    List<PostVote> postVoteList = post.getPostVotes();
    if (checkOppositeVote(postVoteList, user, value)) {
      PostVote postVote = new PostVote();
      postVote.setPost(post);
      postVote.setUser(user);
      postVote.setValue((byte) value);
      postVote.setTime(new Date());
      postVoteList.add(postVote);
    }
    postRepository.save(post);
  }

  private boolean checkDoubleVote(List<PostVote> postVoteList, User user, int value) {
    PostVote doublePostVote = postVoteList.stream()
        .filter(pv -> pv.getUser().getId() == user.getId() && pv.getValue() == value)
        .findFirst()
        .orElse(null);
    return doublePostVote == null;
  }

  private boolean checkOppositeVote(List<PostVote> postVoteList, User user, int value) {
    PostVote postVote = postVoteList.stream()
        .filter(pv -> pv.getUser().getId() == user.getId() && pv.getValue() == value * -1)
        .findFirst()
        .orElse(null);
    if (postVote == null) {
      return true;
    } else {
      postVote.setValue((byte) (value));
      postVote.setTime(new Date());
      return false;
    }
  }
}
