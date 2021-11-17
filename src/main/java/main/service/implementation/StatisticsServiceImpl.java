package main.service.implementation;

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
    List<Post> userPosts = postRepository.getPostsByUser(user.getId());
    List<PostVote> postVoteList = postVotesRepository.getUserPostVote(user.getId());
    return new ResponseEntity<>(
        prepareStatisticsResponse(userPosts, postVoteList), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<StatisticsResponse> getAllStatistics() {
    List<Post> postList = postRepository.getAllPosts();
    List<PostVote> postVoteList = postVotesRepository.getAllPostVote();
    return new ResponseEntity<>(
        prepareStatisticsResponse(postList, postVoteList), HttpStatus.OK);
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
