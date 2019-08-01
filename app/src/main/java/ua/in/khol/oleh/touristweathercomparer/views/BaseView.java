package ua.in.khol.oleh.touristweathercomparer.views;

public interface BaseView<P> {
    void setPresenter(P presenter);

    void showError(String error);

    void showMessage(String message);
}
