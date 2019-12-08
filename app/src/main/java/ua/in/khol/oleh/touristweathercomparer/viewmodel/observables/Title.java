package ua.in.khol.oleh.touristweathercomparer.viewmodel.observables;


import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class Title extends BaseObservable {
    @Bindable
    private int mId;
    @Bindable
    private String mName;
    @Bindable
    private float mCurrent;
    @Bindable
    private String mImage;
    @Bindable
    private String mText;
    @Bindable
    private boolean mVisible;

    public Title(String name) {
        mName = name;
    }

    public Title(String name, float current, String text, String image) {
        mName = name;
        mCurrent = current;
        mImage = image;
        mText = text;
    }

    public Title(int id, String name) {
        mId = id;
        mName = name;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public float getCurrent() {
        return mCurrent;
    }

    public String getImage() {
        return mImage;
    }

    public String getText() {
        return mText;
    }

    public boolean isVisible() {
        return mVisible;
    }

    public void setId(int id) {
        mId = id;
        notifyPropertyChanged(BR.id);
    }

    public void setName(String name) {
        mName = name;
        notifyPropertyChanged(BR.name);
    }

    public void setCurrent(float current) {
        mCurrent = current;
        notifyPropertyChanged(BR.current);
    }

    public void setImage(String image) {
        mImage = image;
        notifyPropertyChanged(BR.image);
    }

    public void setText(String text) {
        mText = text;
        notifyPropertyChanged(BR.text);
    }

    public void setVisible(boolean visible) {
        mVisible = visible;
        notifyPropertyChanged(BR.visible);
    }

    @Override
    public boolean equals(@Nullable Object obj) {

        if (obj == null || obj.getClass() != getClass()) return false;

        if (this == obj) return true;

        Title title = (Title) obj;
        return title.getName().equals(mName);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
