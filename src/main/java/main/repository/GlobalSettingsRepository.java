package main.repository;

import javax.transaction.Transactional;
import main.model.GlobalSetting;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalSettingsRepository extends CrudRepository<GlobalSetting, Integer> {

  @Query("select gs from GlobalSetting gs where gs.code = :code")
  GlobalSetting findSettingByCode(@Param("code") String code);

  @Modifying
  @Transactional
  @Query("update GlobalSetting gs set gs.value = :value where gs.code = :code")
  int setSettings(@Param("code") String code, @Param("value") String value);
}
