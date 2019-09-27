package ua.in.khol.oleh.touristweathercomparer.viewmodel.observables;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.databinding.library.baseAdapters.BR;

import java.util.List;

public class Provider extends BaseObservable {

    @Bindable
    private long mId;
    @Bindable
    private String mUrl;
    @Bindable
    private String mPath;
    @Bindable
    private ObservableList<Forecast> mForecasts;

    public Provider(long id, String url, String path) {
        mId = id;
        mUrl = url;
        mPath = path;
        mForecasts = new ObservableArrayList<>();
    }

    public Provider(String url, String path) {
        mUrl = url;
        mPath = path;
    }

    public long getId() {
        return mId;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getPath() {
        return mPath;
    }

    public ObservableList<Forecast> getForecasts() {
        return mForecasts;
    }

    public void setId(long id) {
        mId = id;
        notifyPropertyChanged(BR.id);
    }

    public void setUrl(String url) {
        mUrl = url;
        notifyPropertyChanged(BR.url);
    }

    public void setPath(String path) {
        mPath = path;
        notifyPropertyChanged(BR.path);
    }

    public void setForecasts(List<Forecast> forecasts) {
        mForecasts.clear();
        mForecasts.addAll(forecasts);
        notifyPropertyChanged(BR.forecasts);
    }

    public void putForecast(Forecast forecast) {
        int index = mForecasts.indexOf(forecast);
        if (index != -1)
            mForecasts.set(index, forecast);
        else
            mForecasts.add(forecast);

        notifyPropertyChanged(BR.forecasts);
    }
}
