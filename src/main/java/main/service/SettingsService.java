package main.service;

import main.api.response.SettingsResponse;
import main.model.repositories.GlobalSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {

    @Autowired
    private final GlobalSettingsRepository globalSettingsRepository;

    public SettingsService(GlobalSettingsRepository globalSettingsRepository) {
        this.globalSettingsRepository = globalSettingsRepository;
    }

    public SettingsResponse getGlobalSettings(){
        SettingsResponse settingsResponse = new SettingsResponse();
        settingsResponse.setMultiuserMode(globalSettingsRepository.findSettingByCode("MULTIUSER_MODE").getValue().equals("YES"));
        settingsResponse.setPostPremoderation(globalSettingsRepository.findSettingByCode("POST_PREMODERATION").getValue().equals("YES"));
        settingsResponse.setStatisticsIsPublic(globalSettingsRepository.findSettingByCode("STATISTICS_IS_PUBLIC").getValue().equals("YES"));
        return settingsResponse;
    }
}
