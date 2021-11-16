package main.service.interfaces;

import java.security.Principal;
import main.dto.response.StatisticsResponse;
import org.springframework.http.ResponseEntity;


public interface StatisticsService {

  /**
   * Получение статистики постов пользователя
   */
  ResponseEntity<StatisticsResponse> getUserStatistics(String userName);

  /**
   * Получение статистики по всем постам блога
   */
  ResponseEntity<StatisticsResponse> getAllStatistics();
}
