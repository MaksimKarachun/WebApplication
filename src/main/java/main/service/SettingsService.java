package main.service;

import lombok.RequiredArgsConstructor;
import main.dto.request.SetSettingRequest;
import main.dto.response.SettingsResponse;
import main.repository.GlobalSettingsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingsService {

  private final GlobalSettingsRepository globalSettingsRepository;

  public ResponseEntity<SettingsResponse> getGlobalSettings() {
    SettingsResponse settingsResponse = new SettingsResponse();
    settingsResponse.setMultiuserMode(
        globalSettingsRepository.findSettingByCode("MULTIUSER_MODE").getValue().equals("YES"));
    settingsResponse.setPostPremoderation(
        globalSettingsRepository.findSettingByCode("POST_PREMODERATION").getValue().equals("YES"));
    settingsResponse.setStatisticsIsPublic(
        globalSettingsRepository.findSettingByCode("STATISTICS_IS_PUBLIC").getValue()
            .equals("YES"));
    return new ResponseEntity<>(settingsResponse, HttpStatus.OK);
  }

  public HttpStatus setGlobalSettings(SetSettingRequest request) {
    String multiuserModeValue = request.isMultiuserMode() ? "YES" : "NO";
    String postModerationValue = request.isPostPremoderation() ? "YES" : "NO";
    String statisticsIsPublic = request.isStatisticsIsPublic() ? "YES" : "NO";
    if (!globalSettingsRepository.findSettingByCode("MULTIUSER_MODE").getValue()
        .equals(multiuserModeValue)) {
      globalSettingsRepository.setSettings("MULTIUSER_MODE", multiuserModeValue);
    }
    if (!globalSettingsRepository.findSettingByCode("POST_PREMODERATION").getValue()
        .equals(postModerationValue)) {
      globalSettingsRepository.setSettings("POST_PREMODERATION", postModerationValue);
    }
    if (!globalSettingsRepository.findSettingByCode("STATISTICS_IS_PUBLIC").getValue()
        .equals(statisticsIsPublic)) {
      globalSettingsRepository.setSettings("STATISTICS_IS_PUBLIC", statisticsIsPublic);
    }
    return HttpStatus.OK;
  }
}
