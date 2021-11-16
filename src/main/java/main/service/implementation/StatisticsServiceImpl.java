package main.service.implementation;

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
import main.service.interfaces.StatisticsService;
import main.stringConst.StringConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import static main.projectEnum.Vote.LIKE;
import static main.projectEnum.Vote.DISLIKE;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

  private final UserRepository userRepository;

  private final PostRepository postRepository;

  private final PostVotesRepository postVotesRepository;

  @Override
  public ResponseEntity<StatisticsResponse> getUserStatistics(String userEmail) {
    User user = Optional.of(userRepository.findByEmail(userEmail))
        .orElseThrow(() -> new UsernameNotFoundException(StringConstant.USER_NOT_FOUND));
    List<PostVote> postVoteList = postVotesRepository.getUserPostVote(user.getId());
    List<Post> userPosts = postRepository.getPostsByUser(user.getId());

    long postCount = userPosts == null ? 0 : userPosts.size();

    long likesCount = postVoteList == null ? 0
        : postVoteList.stream().filter(pv -> pv.getValue() == LIKE.getValue()).count();

    long dislikesCount = postVoteList == null ? 0
        : postVoteList.stream().filter(pv -> pv.getValue() == DISLIKE.getValue()).count();

    long viewsCount =
        userPosts == null ? 0 : userPosts.stream().mapToLong(Post::getViewCount).sum();

    long firstPublication =
        userPosts == null ? 0
            : userPosts.stream().mapToLong(p -> p.getTime().getTime() / 1000).min()
                .orElse(0);

    return new ResponseEntity<>(
        prepareStatisticsResponse(postCount, likesCount, dislikesCount, viewsCount,
            firstPublication), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<StatisticsResponse> getAllStatistics() {
    return null;
  }

  private StatisticsResponse prepareStatisticsResponse(long postCount, long likesCount,
      long dislikesCount, long viewsCount, long firstPublication) {
    StatisticsResponse statisticsResponse = new StatisticsResponse();
    statisticsResponse.setPostsCount(postCount);
    statisticsResponse.setLikesCount(likesCount);
    statisticsResponse.setDislikesCount(dislikesCount);
    statisticsResponse.setViewsCount(viewsCount);
    statisticsResponse.setFirstPublication(firstPublication);
    return statisticsResponse;
  }
}
