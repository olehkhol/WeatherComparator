package ua.in.khol.oleh.touristweathercomparer.viewmodel;

import androidx.databinding.ObservableField;

import ua.in.khol.oleh.touristweathercomparer.model.Repository;

public class AlertViewModel extends BaseViewModel {

    private ObservableField<String> mTitle = new ObservableField<>();
    private ObservableField<String> mMessage = new ObservableField<>();

    public AlertViewModel(Repository repository) {
        super(repository);
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    public ObservableField<String> getTitle() {
        return mTitle;
    }

    public ObservableField<String> getMessage() {
        return mMessage;
    }
}
