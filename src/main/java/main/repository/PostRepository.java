package main.repository;

import java.util.Date;
import main.dto.response.PostInfo;
import main.dto.response.PostWithCount;
import main.dto.response.PostWithLike;
import main.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {

  /**
   * Получение общего количества постов для вывода пользователю
   */
  @Query("select count(p) from Post p " +
      "where moderationStatus = 'ACCEPTED' " +
      "and time < :date " +
      "and isActive = 1")
  Integer getPostCount(@Param("date") Date date);

  /**
   * Метод для модификатора recent (модификатор по умолчанию): сортировать посты по дате публикации,
   * выводить сначала новые
   */
  @Query("select p from Post p " +
      "where moderationStatus = 'ACCEPTED' " +
      "and time < :date " +
      "and isActive = 1")
  Page<Post> findPostsByParamRecent(Pageable pageable, @Param("date") Date date);

  /**
   * Метод для модификатора popular: сортировать посты по количеству комментариев
   */
  @Query("select p as post, count(p) as commentCount from Post as p " +
      "left join PostComment as pc on p.id = pc.post.id " +
      "where p.moderationStatus = 'ACCEPTED' " +
      "and p.time < :date " +
      "and p.isActive = 1 group by p.id")
  Page<PostWithCount> findPostsByParamPopular(Pageable pageable, @Param("date") Date date);


  /**
   * Метод для модификатора best: сортировать посты по количеству лайков
   */
  @Query("select p as post, sum(pv.value) as postLike from Post as p " +
      "left join PostVote as pv on p.id = pv.post.id " +
      "where p.moderationStatus = 'ACCEPTED' " +
      "and p.time < :date " +
      "and p.isActive = 1 group by p.id")
  Page<PostWithLike> findPostsByParamBest(Pageable pageable, @Param("date") Date date);

  /**
   * Метод для модификатора best: сортировать посты по количеству лайков
   */
  @Query("select sum(pv.value) as postLike from Post as p " +
      "left join PostVote as pv on p.id = pv.post.id " +
      "where p.moderationStatus = 'ACCEPTED' " +
      "and p.time < :date " +
      "and p.isActive = 1 group by p.id")
  Long findPostsCountByParamBest(@Param("date") Date date);

  /**
   * Поиск постов по запросу в строке поиска
   */
  @Query("select p from Post as p " +
      "where p.moderationStatus = 'ACCEPTED' " +
      "and p.time < :date " +
      "and p.isActive = 1" +
      "and (p.title like %:query% or p.text like %:query%)")
  List<Post> findPostsByQuery(Pageable pageable, @Param("query") String query, @Param("date") Date date);

  /**
   * Получение всех годов за которые есть посты
   */
  @Query("select distinct year(p.time) as years from Post as p " +
      "where p.moderationStatus = 'ACCEPTED' " +
      "and p.time < :date " +
      "and p.isActive = 1")
  List<Integer> getPostsYears(@Param("date") Date date);

  /**
   * Получение информации по постам в формате год: количество постов
   */
  @Query("select date_format(p.time, '%Y-%m-%d') as date, " +
      "count(p) as postsCount from Post as p " +
      "where p.moderationStatus = 'ACCEPTED' " +
      "and p.time < :date " +
      "and p.isActive = 1 " +
      "and date_format(p.time, '%Y') = :year group by date")
  List<PostInfo> getPostsByYear(@Param("year") String year, @Param("date") Date date);

  /**
   * Получеие постов за указанную дату
   */
  @Query(value = "select * " +
      "from posts as p " +
      "where date_format(p.time, '%Y-%m-%d') = str_to_date(:date, '%Y-%m-%d')",
      nativeQuery = true)
  List<Post> findPostsByDate(Pageable pageable, @Param("date") String date);

  /**
   * Теги и количество использования привязанные к активным постам, утверждённых модератором со
   * временем публикации, не превышающем текущее время
   */
  @Query("select p from Post as p " +
      "join Tag2Post as tp on p.id = tp.post.id " +
      "join Tag as t on t.id = tp.tag.id " +
      "where p.moderationStatus = 'ACCEPTED' " +
      "and p.isActive = 1 " +
      "and p.time < :date " +
      "and t.name like :tag")
  List<Post> findPostsByTag(Pageable pageable, @Param("tag") String tag, @Param("date") Date date);

  /**
   * Получение поста по id
   */
  @Query("select p from Post p " +
      "where id = :id")
  Post getPostById(@Param("id") int id);

  @Query("select p from Post p " +
      "where p.user.id = :id " +
      "and isActive = 0")
  Page<Post> getPostsByUserInactive(Pageable pageable, @Param("id") int id);

  @Query("select p from Post p " +
      "where p.user.id = :id " +
      "and isActive = 1 " +
      "and moderationStatus = 'NEW'")
  Page<Post> getPostsByUserPending(Pageable pageable, @Param("id") int id);

  @Query("select p from Post p " +
      "where p.user.id = :id " +
      "and isActive = 1 " +
      "and moderationStatus = 'DECLINED'")
  Page<Post> getPostsByUserDeclined(Pageable pageable, @Param("id") int id);

  @Query("select p from Post p " +
      "where p.user.id = :id " +
      "and isActive = 1 " +
      "and moderationStatus = 'ACCEPTED'")
  Page<Post> getPostsByUserPublished(Pageable pageable, @Param("id") int id);

  /**
   * ======================================================
   * Получение постов по признаку модерации
   * ======================================================
   */

  /**
   * Получение всех постов которым необходима модерация
   */
  @Query("select p from Post p " +
      "where isActive = 1 " +
      "and moderationStatus = 'NEW'")
  Page<Post> getModerationNewPosts(Pageable pageable);

  /**
   * Получение постов отклоненных пользоваетелем
   */
  @Query("select p from Post p " +
      "where isActive = 1 " +
      "and moderationStatus = 'DECLINED' " +
      "and moderator_id = :id")
  Page<Post> getModerationDeclinedPosts(Pageable pageable, @Param("id") int id);

  /**
   * Получение постов утвержденных пользователем
   */
  @Query("select p from Post p " +
      "where isActive = 1 " +
      "and moderationStatus = 'ACCEPTED' " +
      "and moderator_id = :id")
  Page<Post> getModerationAcceptedPosts(Pageable pageable, @Param("id") int id);

  /**
   * Получение всех постов которым необходима модерация
   */
  @Query("select count(p) from Post p " +
      "where moderationStatus = 'NEW'")
  Integer getCountModerationPosts();


  /**
   * Получение всех постов пользователя
   */
  @Query("select p from Post p " +
      "where isActive = 1 " +
      "and moderationStatus = 'ACCEPTED' " +
      "and p.user.id = :id " +
      "and time < :date")
  List<Post> getPostsByUser(@Param("id") int id, @Param("date") Date date);

  /**
   * Получение всех постов блога
   */
  @Query("select p from Post p " +
      "where isActive = 1 " +
      "and moderationStatus = 'ACCEPTED' " +
      "and time < :date")
  List<Post> getAllPosts(@Param("date") Date date);
}
