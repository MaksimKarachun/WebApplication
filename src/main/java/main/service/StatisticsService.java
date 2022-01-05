package main.service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import main.dto.response.StatisticsResponse;
import main.model.Post;
import main.model.PostVote;
import main.model.User;
import main.repository.PostRepository;
import main.repository.PostVotesRepository;
import main.repository.UserRepository;
import main.config.StringConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import static main.model.appEnum.Vote.LIKE;
import static main.model.appEnum.Vote.DISLIKE;

@Service
@RequiredArgsConstructor
public class StatisticsService {

  private final UserRepository userRepository;

  private final PostRepository postRepository;

  private final PostVotesRepository postVotesRepository;

  private final SettingsService settingsService;

  public ResponseEntity<StatisticsResponse> getUserStatistics(String userEmail) {
    User user = Optional.of(userRepository.findByEmail(userEmail))
        .orElseThrow(() -> new UsernameNotFoundException(StringConstant.USER_NOT_FOUND));
    List<Post> userPosts = postRepository.getPostsByUser(user.getId(), new Date());
    List<PostVote> postVoteList = postVotesRepository.getUserPostVote(user.getId());
    return new ResponseEntity<>(
        prepareStatisticsResponse(userPosts, postVoteList), HttpStatus.OK);
  }

  public ResponseEntity<StatisticsResponse> getAllStatistics(Principal principal) {
    if (settingsService.getStatisticIsPublicSetting()) {
      List<Post> postList = postRepository.getAllPosts(new Date());
      List<PostVote> postVoteList = postVotesRepository.getAllPostVote();
      return new ResponseEntity<>(
          prepareStatisticsResponse(postList, postVoteList), HttpStatus.OK);
    } else {
      if (principal == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      String userEmail = principal.getName();
      User user = Optional.of(userRepository.findByEmail(userEmail))
          .orElseThrow(() -> new UsernameNotFoundException(StringConstant.USER_NOT_FOUND));
      if (user == null || !user.isModerator()) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      } else {
        List<Post> postList = postRepository.getAllPosts(new Date());
        List<PostVote> postVoteList = postVotesRepository.getAllPostVote();
        return new ResponseEntity<>(
            prepareStatisticsResponse(postList, postVoteList), HttpStatus.OK);
      }
    }
  }

  private StatisticsResponse prepareStatisticsResponse(List<Post> postList, List<PostVote> postVoteList) {

    long postCount = postList == null ? 0 : postList.size();

    long likesCount = postVoteList == null ? 0
        : postVoteList.stream().filter(pv -> pv.getValue() == LIKE.getValue()).count();

    long dislikesCount = postVoteList == null ? 0
        : postVoteList.stream().filter(pv -> pv.getValue() == DISLIKE.getValue()).count();

    long viewsCount =
        postList == null ? 0 : postList.stream().mapToLong(Post::getViewCount).sum();

    long firstPublication =
        postList == null ? 0
            : postList.stream().mapToLong(p -> p.getTime().getTime() / 1000).min()
                .orElse(0);

    StatisticsResponse statisticsResponse = new StatisticsResponse();
    statisticsResponse.setPostsCount(postCount);
    statisticsResponse.setLikesCount(likesCount);
    statisticsResponse.setDislikesCount(dislikesCount);
    statisticsResponse.setViewsCount(viewsCount);
    statisticsResponse.setFirstPublication(firstPublication);
    return statisticsResponse;
  }
}
