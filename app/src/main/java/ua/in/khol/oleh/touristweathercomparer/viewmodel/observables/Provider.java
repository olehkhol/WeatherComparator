package ua.in.khol.oleh.touristweathercomparer.viewmodel.observables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(indices = {@Index(value = "title_id", unique = true)})
public class Provider {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "title_id")
    private long mTitleId;

    @ColumnInfo(name = "url")
    private String mUrl;

    @ColumnInfo(name = "path")
    private String mPath;

    // TODO must be one to many
    @Ignore
    private List<Forecast> mForecasts;

    @Ignore
    public Provider(long id, String url, String path) {
        mId = id;
        mUrl = url;
        mPath = path;
    }

    public Provider(String url, String path) {
        mUrl = url;
        mPath = path;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public long getTitleId() {
        return mTitleId;
    }

    public void setTitleId(long titleId) {
        mTitleId = titleId;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public List<Forecast> getForecasts() {
        return mForecasts;
    }

    public void setForecasts(List<Forecast> forecasts) {
        mForecasts = forecasts;
    }

//    @Override
//    public boolean equals(@Nullable Object obj) {
//        if (obj == null || obj.getClass() != getClass()) return false;
//
//        if (this == obj) return true;
//
//        Provider title = (Provider) obj;
//        return title.getTitleId() == mTitleId;
//    }
//
//    @Override
//    public int hashCode() {
//        return super.hashCode();
//    }
}
