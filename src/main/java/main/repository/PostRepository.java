package main.repository;

import main.dto.response.PostInfo;
import main.model.Post;
import main.model.User;
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
            "and time < sysdate()" +
            "and isActive = 1")
    Integer getPostCount();

    /**
    * Метод для модификатора recent (модификатор по умолчанию):
    * сортировать посты по дате публикации, выводить сначала новые
    */
    @Query("select p from Post p " +
            "where moderationStatus = 'ACCEPTED' " +
            "and time < sysdate()" +
            "and isActive = 1")
    List<Post> findPostsByParamRecent(Pageable pageable);

    /**
     * Метод для модификатора popular:
     * сортировать посты по количеству комментариев
     */
    @Query("select p, count(p) as commentCount from Post as p " +
            "left join PostComment as pc on p.id = pc.post.id " +
            "where p.moderationStatus = 'ACCEPTED' " +
            "and p.time < sysdate()" +
            "and p.isActive = 1 group by p.id")
    List<Post> findPostsByParamPopular(Pageable pageable);

    /**
     * Метод для модификатора best:
     * сортировать посты по количеству лайков
     */
    @Query("select p, sum(pv.value) as postLike from Post as p " +
            "left join PostVote as pv on p.id = pv.post.id " +
            "where p.moderationStatus = 'ACCEPTED' " +
            "and p.time < sysdate()" +
            "and p.isActive = 1 group by p.id")
    List<Post> findPostsByParamBest(Pageable pageable);

    /**
     * Поиск постов по запросу в строке поиска
     */
    @Query("select p from Post as p " +
            "where p.moderationStatus = 'ACCEPTED' " +
            "and p.time < sysdate()" +
            "and p.isActive = 1" +
            "and (p.title like %:query% or p.text like %:query%)")
    List<Post> findPostsByQuery(Pageable pageable, @Param("query") String query);

    /**
     * Получение всех годов за которые есть посты
     */
    @Query("select distinct year(p.time) as years from Post as p " +
            "where p.moderationStatus = 'ACCEPTED' " +
            "and p.time < sysdate()" +
            "and p.isActive = 1")
    List<Integer> getPostsYears();

    /**
     * Получение информации по постам в формате год: количество постов
     */
    @Query("select date_format(p.time, '%Y-%m-%d') as date, " +
            "count(p) as postsCount from Post as p " +
            "where p.moderationStatus = 'ACCEPTED' " +
            "and p.time < sysdate()" +
            "and p.isActive = 1 " +
            "and date_format(p.time, '%Y') = :year group by date")
    List<PostInfo> getPostsByYear(@Param("year") String year);

    /**
     * Получеие постов за указанную дату
     */
    @Query(value = "select * " +
            "from posts as p " +
            "where date_format(p.time, '%Y-%d-%m') = str_to_date(:date, '%Y-%d-%m')",
            nativeQuery = true)
    List<Post> findPostsByDate(Pageable pageable, @Param("date") String date);

    /**
     * Теги и количество использования привязанные к активным постам,
     * утверждённых модератором со временем публикации, не превышающем
     * текущее время
    */
    @Query("select p from Post as p " +
            "join Tag2Post as tp on p.id = tp.post.id " +
            "join Tag as t on t.id = tp.tag.id " +
            "where p.moderationStatus = 'ACCEPTED' " +
            "and p.isActive = 1 " +
            "and p.time < sysdate()" +
            "and t.name like :tag")
    List<Post> findPostsByTag(Pageable pageable, @Param("tag") String tag);

    /**
     * Получение поста по id учитывая ограничения
     */
    @Query("select p from Post p " +
            "where moderationStatus = 'ACCEPTED' " +
            "and time < sysdate()" +
            "and isActive = 1 " +
            "and id = :id")
    Post getPostById(@Param("id") int id);

    @Query("select p from Post p " +
        "where p.user.id = :id " +
        "and isActive = 0")
    List<Post> getPostsByUserInactive(Pageable pageable, @Param("id") int id);

    @Query("select p from Post p " +
        "where p.user.id = :id " +
        "and isActive = 1 " +
        "and moderationStatus = 'NEW'")
    List<Post> getPostsByUserPending(Pageable pageable, @Param("id") int id);

    @Query("select p from Post p " +
        "where p.user.id = :id " +
        "and isActive = 1 " +
        "and moderationStatus = 'DECLINED'")
    List<Post> getPostsByUserDeclined(Pageable pageable, @Param("id") int id);

    @Query("select p from Post p " +
        "where p.user.id = :id " +
        "and isActive = 1 " +
        "and moderationStatus = 'ACCEPTED'")
    List<Post> getPostsByUserAccepted(Pageable pageable, @Param("id") int id);

    @Query("select count(p) from Post p " +
        "where p.user.id = :id " +
        "and isActive = 0")
    Integer getCountPostsByUserInactive(@Param("id") int id);

    @Query("select count(p) from Post p " +
        "where p.user.id = :id " +
        "and isActive = 1 " +
        "and moderationStatus = 'NEW'")
    Integer getCountPostsByUserPending(@Param("id") int id);

    @Query("select count(p) from Post p " +
        "where p.user.id = :id " +
        "and isActive = 1 " +
        "and moderationStatus = 'DECLINED'")
    Integer getCountPostsByUserDeclined(@Param("id") int id);

    @Query("select count(p) from Post p " +
        "where p.user.id = :id " +
        "and isActive = 1 " +
        "and moderationStatus = 'ACCEPTED'")
    Integer getCountPostsByUserAccepted(@Param("id") int id);

    /**
     * ======================================================
     * Получение постов по признаку модерации
     * ======================================================
     */

    /**
     *Получение всех постов которым необходима модерация
     */
    @Query("select p from Post p " +
        "where isActive = 1 " +
        "and moderationStatus = 'NEW'")
    List<Post> getModerationNewPosts(Pageable pageable);

    /**
     *Получение постов отклоненных пользоваетелем
     */
    @Query("select p from Post p " +
        "where isActive = 1 " +
        "and moderationStatus = 'DECLINED' " +
        "and moderator_id = :id")
    List<Post> getModerationDeclinedPosts(Pageable pageable, @Param("id") int id);

    /**
     *Получение постов утвержденных пользователем
     */
    @Query("select p from Post p " +
        "where isActive = 1 " +
        "and moderationStatus = 'ACCEPTED' " +
        "and moderator_id = :id")
    List<Post> getModerationAcceptedPosts(Pageable pageable, @Param("id") int id);

    @Query("select count(p) from Post p " +
        "where isActive = 1 " +
        "and moderationStatus = 'NEW'")
    Integer getModerationNewPostsCount();

    @Query("select count(p) from Post p " +
        "where isActive = 1 " +
        "and moderationStatus = 'DECLINED' " +
        "and moderator_id = :id")
    Integer getModerationDeclinedPostsCount(@Param("id") int id);

    @Query("select count(p) from Post p " +
        "where isActive = 1 " +
        "and moderationStatus = 'ACCEPTED' " +
        "and moderator_id = :id")
    Integer getModerationAcceptedPostsCount(@Param("id") int id);

}
