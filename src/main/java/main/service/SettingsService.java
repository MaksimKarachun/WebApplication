package main.service;

import lombok.RequiredArgsConstructor;
import main.dto.response.SettingsResponse;
import main.repository.GlobalSettingsRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingsService {

    private final GlobalSettingsRepository globalSettingsRepository;

    public SettingsResponse getGlobalSettings(){
        SettingsResponse settingsResponse = new SettingsResponse();
        settingsResponse.setMultiuserMode(globalSettingsRepository.findSettingByCode("MULTIUSER_MODE").getValue().equals("YES"));
        settingsResponse.setPostPremoderation(globalSettingsRepository.findSettingByCode("POST_PREMODERATION").getValue().equals("YES"));
        settingsResponse.setStatisticsIsPublic(globalSettingsRepository.findSettingByCode("STATISTICS_IS_PUBLIC").getValue().equals("YES"));
        return settingsResponse;
    }
}
