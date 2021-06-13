package main.repository;

import main.DTO.dtoObj.TagCount;
import main.model.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TagRepository extends CrudRepository<Tag, Integer> {

    /*
     теги и количество использования привязанные к активным постам, утверждённых модератором со временем
     публикации, не превышающем текущее время
    */
    @Query("select t.name as name, count(t.name) as count from Tag as t " +
            "join Tag2Post as tp on t.id = tp.tag.id " +
            "join Post as p on p.id = tp.post.id " +
            "where p.moderationStatus = 'ACCEPTED' " +
            "and p.isActive = 1 " +
            "and p.time < sysdate()" +
            "group by t.name")
    List<TagCount> findDefaultTags();
}
