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
            "and isActive = 1 order by time desc")
    List<Post> findPostsByParamRecent(Pageable pageable);
}
