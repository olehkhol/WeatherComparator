package ua.in.khol.oleh.touristweathercomparer.views.callbacks;

public interface MapaCallbacks {
    void onMapaClicked(double latitude, double longitude);

    void onMapaLocationClicked(double latitude, double longitude);

    void onMapaLocationButtonClicked();
}
