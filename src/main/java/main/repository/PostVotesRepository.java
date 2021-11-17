package main.repository;

import java.util.List;
import main.model.PostVote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostVotesRepository extends PagingAndSortingRepository<PostVote, Long> {

  /**
   * Получение всех голосов (like/dislike) пользователя
   */
  @Query("select pv from PostVote pv " +
      "where pv.user.id = :id " +
      "and pv.post.moderationStatus = 'ACCEPTED' " +
      "and pv.post.isActive = 1 " +
      "and pv.post.time < sysdate()")
  List<PostVote> getUserPostVote(@Param("id") int id);

  /**
   * Получение всех голосов (like/dislike) к действующим постам блога
   */
  @Query("select pv from PostVote pv " +
      "where pv.post.moderationStatus = 'ACCEPTED' " +
      "and pv.post.isActive = 1 " +
      "and pv.post.time < sysdate()")
  List<PostVote> getAllPostVote();
}
