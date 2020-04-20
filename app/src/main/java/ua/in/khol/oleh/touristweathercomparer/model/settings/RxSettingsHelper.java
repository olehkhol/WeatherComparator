package ua.in.khol.oleh.touristweathercomparer.model.settings;

import android.content.Context;

import ua.in.khol.oleh.touristweathercomparer.model.Helper;
import ua.in.khol.oleh.touristweathercomparer.model.settings.preferences.RxPreferencesSettings;

public class RxSettingsHelper implements RxSettings, Helper {
    public static final int SHARED_PREFERENCES = 0;
    private static final int ACCOUNT_MANAGER = 1;

    private final Context mContext;

    private RxSettings mRxSettings;

    public RxSettingsHelper(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public synchronized void setup(int source) {
        switch (source) {
            case SHARED_PREFERENCES:
            case ACCOUNT_MANAGER: // Not implemented yet - keep it for future
            default:
                mRxSettings = new RxPreferencesSettings(mContext);
                break;
        }
    }

    @Override
    public synchronized String getLanguage() {
        return mRxSettings.getLanguage();
    }

    @Override
    public synchronized void putLanguage(String language) {
        mRxSettings.putLanguage(language);
    }

    @Override
    public synchronized int getLanguageIndex() {
        return mRxSettings.getLanguageIndex();
    }

    @Override
    public synchronized void putLanguageIndex(int index) {
        mRxSettings.putLanguageIndex(index);
    }

    @Override
    public int getPressureIndex() {
        return mRxSettings.getPressureIndex();
    }

    @Override
    public void putPressureIndex(int index) {
        mRxSettings.putPressureIndex(index);
    }

    @Override
    public int getTemperatureIndex() {
        return mRxSettings.getTemperatureIndex();
    }

    @Override
    public void putTemperatureIndex(int index) {
        mRxSettings.putTemperatureIndex(index);
    }

    @Override
    public int getSpeedIndex() {
        return mRxSettings.getSpeedIndex();
    }

    @Override
    public void putSpeedIndex(int index) {
        mRxSettings.putSpeedIndex(index);
    }

    @Override
    public synchronized double getLat() {
        return mRxSettings.getLat();
    }

    @Override
    public synchronized void putLat(double lat) {
        mRxSettings.putLat(lat);
    }

    @Override
    public synchronized double getLon() {
        return mRxSettings.getLon();
    }

    @Override
    public synchronized void putLon(double lon) {
        mRxSettings.putLon(lon);
    }

    @Override
    public synchronized void putSetting(Settings settings) {
        mRxSettings.putSetting(settings);
    }

    @Override
    public Settings getSettings() {
        return mRxSettings.getSettings();
    }
}
