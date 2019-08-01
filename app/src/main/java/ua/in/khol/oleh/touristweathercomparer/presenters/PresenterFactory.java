package ua.in.khol.oleh.touristweathercomparer.presenters;

public interface PresenterFactory<P extends BasePresenter> {
    P create();
}
