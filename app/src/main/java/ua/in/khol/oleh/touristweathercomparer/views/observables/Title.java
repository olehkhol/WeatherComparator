package ua.in.khol.oleh.touristweathercomparer.views.observables;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class Title extends BaseObservable {
    private String name;
    private float current;
    private String src;
    private String text;


    public Title(String name) {
        this.name = name;
    }

    public Title(String name, float current, String text, String src) {
        this(name);
        this.current = current;
        this.src = src;
        this.text = text;
    }

    @Bindable
    public String getName() {
        return name;
    }

    @Bindable
    public float getCurrent() {
        return current;
    }

    @Bindable
    public String getSrc() {
        return src;
    }

    @Bindable
    public String getText() {
        return text;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public void setCurrent(float current) {
        this.current = current;
        notifyPropertyChanged(BR.current);
    }

    public void setSrc(String src) {
        this.src = src;
        notifyPropertyChanged(BR.src);
    }

    public void setText(String text) {
        this.text = text;
        notifyPropertyChanged(BR.text);
    }
}
