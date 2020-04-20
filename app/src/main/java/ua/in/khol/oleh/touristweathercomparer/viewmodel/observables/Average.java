package ua.in.khol.oleh.touristweathercomparer.viewmodel.observables;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import java.util.ArrayList;
import java.util.List;

public class Average extends BaseObservable {

    private int mDate;
    private float mLow;
    private float mHigh;
    private int mPressure;
    private float mSpeed;
    private int mDegree;
    private int mHumidity;
    private final List<Canape> mCanapes = new ArrayList<>();

    public Average(int date, float low, float high, List<Canape> canapes) {
        mDate = date;
        mLow = low;
        mHigh = high;
        setCanapes(canapes);
    }

    @Bindable
    public float getLow() {
        return mLow;
    }

    public void setLow(float low) {
        mLow = low;
        notifyPropertyChanged(BR.low);
    }

    @Bindable
    public float getHigh() {
        return mHigh;
    }

    public void setHigh(float high) {
        mHigh = high;
        notifyPropertyChanged(BR.high);
    }

    @Bindable
    public int getDate() {
        return mDate;
    }

    public void setDate(int date) {
        mDate = date;
        notifyPropertyChanged(BR.date);
    }

    @Bindable
    public List<Canape> getCanapes() {
        return mCanapes;
    }

    private void setCanapes(List<Canape> canapes) {
        mCanapes.clear();
        mCanapes.addAll(canapes);
        notifyPropertyChanged(BR.canapes);
    }

    @Bindable
    public int getPressure() {
        return mPressure;
    }

    public void setPressure(int pressure) {
        mPressure = pressure;
        notifyPropertyChanged(BR.pressure);
    }

    @Bindable
    public float getSpeed() {
        return mSpeed;
    }

    public void setSpeed(float speed) {
        mSpeed = speed;
        notifyPropertyChanged(BR.speed);
    }

    @Bindable
    public int getDegree() {
        return mDegree;
    }

    public void setDegree(int degree) {
        mDegree = degree;
        notifyPropertyChanged(BR.degree);
    }

    @Bindable
    public int getHumidity() {
        return mHumidity;
    }

    public void setHumidity(int humidity) {
        mHumidity = humidity;
        notifyPropertyChanged(BR.humidity);
    }

    public Average deepCopy() {
        List<Canape> canapes = new ArrayList<>();
        for (Canape canape : mCanapes)
            canapes.add(new Canape(canape.mText, canape.mImage));
        Average average = new Average(mDate, mLow, mHigh, canapes);
        average.setPressure(mPressure);
        average.setSpeed(mSpeed);
        average.setDegree(mDegree);
        average.setHumidity(mHumidity);

        return average;
    }

    public static class Canape extends BaseObservable {
        private String mText;
        private String mImage;

        public Canape(String text, String image) {
            mText = text;
            mImage = image;
        }

        @Bindable
        public String getText() {
            return mText;
        }

        public void setText(String text) {
            mText = text;
            notifyPropertyChanged(BR.text);
        }

        @Bindable
        public String getImage() {
            return mImage;
        }

        public void setImage(String image) {
            mImage = image;
            notifyPropertyChanged(BR.image);
        }
    }
}
