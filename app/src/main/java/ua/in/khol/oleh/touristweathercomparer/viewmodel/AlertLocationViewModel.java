package ua.in.khol.oleh.touristweathercomparer.viewmodel;

import ua.in.khol.oleh.touristweathercomparer.model.Repository;

public class AlertLocationViewModel extends BaseViewModel {

    public AlertLocationViewModel(Repository repository) {
        super(repository);
    }

    @Override
    public void update() {
        getRepository().update();
    }
}
