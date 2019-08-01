package ua.in.khol.oleh.touristweathercomparer.presenters;

public interface BasePresenter<V> {
    void attachView(V view);

    void detachView();

    void destroy();
}
