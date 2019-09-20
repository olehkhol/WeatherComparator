package ua.in.khol.oleh.touristweathercomparer.viewmodel.observables;


import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"city_id", "name"}, unique = true)})
public class Title {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "city_id")
    private long mCityId;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "current")
    private float mCurrent;

    @ColumnInfo(name = "src")
    private String mSrc;

    @ColumnInfo(name = "text")
    private String mText;

    @Ignore
    public Title(String name, float current, String text, String src) {
        mName = name;
        mCurrent = current;
        mSrc = src;
        mText = text;
    }

    public Title(long cityId, String name, float current, String src, String text) {
        mCityId = cityId;
        mName = name;
        mCurrent = current;
        mSrc = src;
        mText = text;
    }

    @Ignore
    public Title(long id, long cityId, String name, float current, String src, String text) {
        mId = id;
        mCityId = cityId;
        mName = name;
        mCurrent = current;
        mSrc = src;
        mText = text;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public long getCityId() {
        return mCityId;
    }

    public void setCityId(long cityId) {
        mCityId = cityId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public float getCurrent() {
        return mCurrent;
    }

    public void setCurrent(float current) {
        mCurrent = current;
    }

    public String getSrc() {
        return mSrc;
    }

    public void setSrc(String src) {
        mSrc = src;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    @Override
    public boolean equals(@Nullable Object obj) {

        if (obj == null || obj.getClass() != getClass()) return false;

        if (this == obj) return true;

        Title title = (Title) obj;
        return title.getCityId() == mCityId && title.getName().equals(mName);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
