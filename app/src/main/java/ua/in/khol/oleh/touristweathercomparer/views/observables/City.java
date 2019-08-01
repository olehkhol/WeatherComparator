package ua.in.khol.oleh.touristweathercomparer.views.observables;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import ua.in.khol.oleh.touristweathercomparer.BR;

public class City extends BaseObservable {
    private String name;
    private double latitude, longitude;

    public City() {
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
        notifyPropertyChanged(BR.latitude);
    }

    @Bindable
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
        notifyPropertyChanged(BR.longitude);
    }
}
