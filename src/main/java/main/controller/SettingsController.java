package main.controller;

import lombok.RequiredArgsConstructor;
import main.dto.request.SetSettingRequest;
import main.dto.response.SettingsResponse;
import main.service.SettingsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/settings")
public class SettingsController {

  private final SettingsService settingsService;

  @GetMapping
  public ResponseEntity<SettingsResponse> getSettings() {
    return settingsService.getGlobalSettings();
  }

  @PutMapping
  @PreAuthorize("hasAuthority('user:moderate')")
  public HttpStatus setSettings(@RequestBody SetSettingRequest setSettingRequest) {
    return settingsService.setGlobalSettings(setSettingRequest);
  }
}
