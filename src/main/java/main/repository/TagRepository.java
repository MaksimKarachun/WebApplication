package main.repository;

import main.dto.response.TagCount;
import main.model.Tag;
import net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import org.springframework.data.repository.query.Param;

public interface TagRepository extends CrudRepository<Tag, Integer> {

  /**
   * Теги и количество использования привязанные к активным постам, утверждённых модератором со
   * временем публикации, не превышающем текущее время
   */
  @Query("select t.name as name, count(t.name) as count from Tag as t " +
      "join Tag2Post as tp on t.id = tp.tag.id " +
      "join Post as p on p.id = tp.post.id " +
      "where p.moderationStatus = 'ACCEPTED' " +
      "and p.isActive = 1 " +
      "and p.time < sysdate()" +
      "group by t.name")
  List<TagCount> findDefaultTags();

  @Query("select t " +
      "from Tag t " +
      "where t.name = :name")
  Tag findByName(@Param("name") String name);
}
