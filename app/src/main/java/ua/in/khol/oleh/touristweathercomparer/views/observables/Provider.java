package ua.in.khol.oleh.touristweathercomparer.views.observables;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import java.util.List;


public class Provider extends BaseObservable {
    private String url;
    private String path;
    private List<Forecast> forecasts;

    public Provider(String url, String path) {
        this.url = url;
        this.path = path;
    }

    public Provider() {

    }

    @Bindable
    public String getUrl() {
        return url;
    }

    @Bindable
    public String getPath() {
        return path;
    }

    @Bindable
    public List<Forecast> getForecasts() {
        return forecasts;
    }


    public void setUrl(String url) {
        this.url = url;
        notifyPropertyChanged(BR.url);
    }

    public void setPath(String path) {
        this.path = path;
        notifyPropertyChanged(BR.path);
    }

    public void setForecasts(List<Forecast> forecasts) {
        this.forecasts = forecasts;
        notifyPropertyChanged(BR.forecasts);
    }

    public void modifyForecasts(List<Forecast> forecasts) {

    }
}
