package ua.in.khol.oleh.touristweathercomparer.viewmodel;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;

import ua.in.khol.oleh.touristweathercomparer.model.Repository;

public class SettingsViewModel extends BaseViewModel {

    private boolean mCriticalChanges;
    // Fields for Data Binding
    private ObservableBoolean mCelsius = new ObservableBoolean();
    private ObservableInt mSelected = new ObservableInt();

    public SettingsViewModel(Repository repository) {
        super(repository);
        setCelsius(getRepository().getPrefCelsius());
        setSelected(getRepository().getPrefLanguageIndex());
    }

    @Override
    public void start() {
        mCriticalChanges = false;
    }

    @Override
    public void stop() {
        if (mCriticalChanges)
            getRepository().updatePreferences();
    }

    // POJOs for Data Binding
    public boolean getCelsius() {
        return mCelsius.get();
    }

    public void setCelsius(boolean celsius) {
        if (mCelsius.get() != celsius) {
            mCelsius.set(celsius);
            getRepository().putPrefCelsius(celsius);
            mCriticalChanges = true;
        }
    }

    public int getSelected() {
        return mSelected.get();
    }

    public void setSelected(int selected) {
        if (mSelected.get() != selected) {
            mSelected.set(selected);
            getRepository().putPrefLanguageIndex(selected);
            mCriticalChanges = true;
        }
    }
}