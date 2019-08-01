package ua.in.khol.oleh.touristweathercomparer.views.observables;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import ua.in.khol.oleh.touristweathercomparer.BR;

public class Forecast extends BaseObservable {
    private int date;
    private float low;
    private float high;
    private String text;
    private String src;
    private String humidity;

    public Forecast() {
    }

    @Bindable
    public int getDate() {
        return date;
    }

    @Bindable
    public float getLow() {
        return low;
    }

    @Bindable
    public float getHigh() {
        return high;
    }

    @Bindable
    public String getText() {
        return text;
    }

    @Bindable
    public String getSrc() {
        return src;
    }

    @Bindable
    public String getHumidity() {
        return humidity;
    }


    public void setDate(int date) {
        this.date = date;
        notifyPropertyChanged(BR.date);
    }

    public void setLow(float low) {
        this.low = low;
        notifyPropertyChanged(BR.low);
    }

    public void setHigh(float high) {
        this.high = high;
        notifyPropertyChanged(BR.high);
    }

    public void setText(String text) {
        this.text = text;
        notifyPropertyChanged(BR.text);
    }

    public void setSrc(String src) {
        this.src = src;
        notifyPropertyChanged(BR.src);
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
        notifyPropertyChanged(BR.humidity);
    }
}
