package ua.in.khol.oleh.touristweathercomparer.viewmodel;

import androidx.databinding.ObservableField;

import ua.in.khol.oleh.touristweathercomparer.model.Repository;

public class AlertViewModel extends BaseViewModel {

    private final ObservableField<String> mTitle = new ObservableField<>();
    private final ObservableField<String> mMessage = new ObservableField<>();

    public AlertViewModel(Repository repository) {
        super(repository);
    }

    @Override
    public void refresh() {

    }

    public ObservableField<String> getTitle() {
        return mTitle;
    }

    public ObservableField<String> getMessage() {
        return mMessage;
    }
}
