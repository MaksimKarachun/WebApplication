package main.repository;

import main.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {

    /*
    * Метод для модификатора recent (модификатор по умолчанию):
    * сортировать посты по дате публикации, выводить сначала новые
    */
    @Query("select p from Post p " +
            "where moderationStatus = 'ACCEPTED' " +
            "and time < sysdate()" +
            "and isActive = 1")
    List<Post> findPostsByParamRecent(Pageable pageable);

    @Query("select p, count(p) as commentCount from Post as p " +
            "left join PostComment as pc on p.id = pc.post.id " +
            "where p.moderationStatus = 'ACCEPTED' " +
            "and p.time < sysdate()" +
            "and p.isActive = 1 group by p.id")
    List<Post> findPostsByParamPopular(Pageable pageable);

    @Query("select p, count(p) as postLike from Post as p " +
            "left join PostVote as pv on p.id = pv.post.id " +
            "where p.moderationStatus = 'ACCEPTED' " +
            "and p.time < sysdate()" +
            "and p.isActive = 1 group by p.id")
    List<Post> findPostsByParamBest(Pageable pageable);
}
