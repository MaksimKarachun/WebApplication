package main.model.repositories;

import main.model.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {

    /*
    * Метод для модификатора recent (модификатор по умолчанию):
    * сортировать посты по дате публикации, выводить сначала новые
    */
    @Query(value = "select *, t.num from (select *, (@i \\:= @i+1) num from posts, (select @i \\:= 0) x " +
            "where moderation_status = 'ACCEPTED' " +
            "and time < sysdate() " +
            "and is_active = 1 order by time desc limit :limit) t " +
            "where t.num > :offset",
            nativeQuery = true)
    List<Post> findPostsByParamRecent(@Param("offset") int offset, @Param("limit") int limit);
}
