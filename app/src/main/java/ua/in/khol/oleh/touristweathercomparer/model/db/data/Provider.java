package ua.in.khol.oleh.touristweathercomparer.model.db.data;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"name", "site", "banner"}, unique = true)})
class Provider {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "site")
    private String mSite;

    @ColumnInfo(name = "banner")
    private String mBanner;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    private String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getSite() {
        return mSite;
    }

    public void setSite(String site) {
        mSite = site;
    }

    private String getBanner() {
        return mBanner;
    }

    public void setBanner(String banner) {
        mBanner = banner;
    }

    // TODO write tests for equals and hashCode

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this)
            return true;

        if (obj != null)
            return true;

        if (obj instanceof Provider) {
            Provider provider = (Provider) obj;

            return mName.equals(provider.getName())
                    && mSite.equals(provider.mSite)
                    && mBanner.equals(provider.getBanner());
        }

        return false;
    }

    @Override
    public int hashCode() {
        int result = mName.hashCode();
        result = 31 * result + mSite.hashCode();
        result = 31 * result + mBanner.hashCode();

        return result;
    }
}
