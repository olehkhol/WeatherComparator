package ua.in.khol.oleh.touristweathercomparer.ui;

import ua.in.khol.oleh.touristweathercomparer.data.settings.SettingsRepository;

public abstract class ActivityViewModel extends BaseViewModel {

    private final SingleLiveEvent<String> languageEvent;
    private final SingleLiveEvent<Integer> themeCodeEvent;

    public ActivityViewModel(SettingsRepository settingsRepository) {

        languageEvent = new SingleLiveEvent<>(settingsRepository.retrieveLanguage());
        execute(settingsRepository.getLanguageCodeObservable(), languageEvent::setValue);
        themeCodeEvent = new SingleLiveEvent<>(settingsRepository.retrieveThemeCode());
        execute(settingsRepository.getThemeCodeObservable(), themeCodeEvent::setValue);
    }

    public String getLanguageCode() {
        return languageEvent.getValue();
    }

    public Integer getThemeCode() {
        return themeCodeEvent.getValue();
    }

    public SingleLiveEvent<String> getLanguageEvent() {
        return languageEvent;
    }

    public SingleLiveEvent<Integer> getThemeCodeEvent() {
        return themeCodeEvent;
    }
}