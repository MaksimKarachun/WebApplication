package main.controller;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import main.dto.response.StatisticsResponse;
import main.service.interfaces.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StatisticsController {

  private final StatisticsService statisticsService;

  @GetMapping("/api/statistics/my")
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity<StatisticsResponse> getUserStatistics(Principal principal) {
    return statisticsService.getUserStatistics(principal.getName());
  }

  @GetMapping("/api/statistics/all")
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity<StatisticsResponse> getAllStatistics() {
    return statisticsService.getAllStatistics();
  }
}
