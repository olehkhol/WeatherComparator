package ua.in.khol.oleh.touristweathercomparer.model.settings;

public class Settings implements Cloneable {
    private int mLanguageIndex;
    private int mPressureIndex;
    private int mTemperatureIndex;
    private int mSpeedIndex;

    public Settings(int languageIndex, int pressureIndex, int temperatureIndex, int speedIndex) {
        mLanguageIndex = languageIndex;
        mPressureIndex = pressureIndex;
        mTemperatureIndex = temperatureIndex;
        mSpeedIndex = speedIndex;
    }

    public static Settings copy(Settings settings) {
        return new Settings(settings.getLanguageIndex(), settings.getPressureIndex(),
                settings.getTemperatureIndex(), settings.getSpeedIndex());
    }

    public int getLanguageIndex() {
        return mLanguageIndex;
    }

    public void setLanguageIndex(int index) {
        mLanguageIndex = index;
    }

    public int getPressureIndex() {
        return mPressureIndex;
    }

    public void setPressureIndex(int pressureIndex) {
        mPressureIndex = pressureIndex;
    }

    public int getTemperatureIndex() {
        return mTemperatureIndex;
    }

    public void setTemperatureIndex(int temperatureIndex) {
        mTemperatureIndex = temperatureIndex;
    }

    public int getSpeedIndex() {
        return mSpeedIndex;
    }

    public void setSpeedIndex(int speedIndex) {
        mSpeedIndex = speedIndex;
    }
}
