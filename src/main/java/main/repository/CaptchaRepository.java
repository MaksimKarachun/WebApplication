package main.repository;

import main.model.CaptchaCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CaptchaRepository extends JpaRepository<CaptchaCode, Integer> {

  /**
   * Метод для очистки устаревших записей в таблицу. Срок хранения - 1ч.
   */
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Transactional
  @Query("delete " +
      "from CaptchaCode cc " +
      "where cc.time < (sysdate() - 3600)")
  Integer deleteOldCaptcha();

  CaptchaCode findBySecretCode(String requestSecret);
}
