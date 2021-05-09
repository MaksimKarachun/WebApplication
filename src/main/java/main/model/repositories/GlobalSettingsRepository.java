package main.model.repositories;

import main.model.GlobalSetting;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalSettingsRepository extends CrudRepository<GlobalSetting, Integer> {

    @Query("select gs from GlobalSetting gs where gs.code = :code")
    GlobalSetting findSettingByCode(@Param("code") String code);
}
