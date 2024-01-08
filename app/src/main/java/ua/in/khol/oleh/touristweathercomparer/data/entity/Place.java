package ua.in.khol.oleh.touristweathercomparer.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class Place implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "latitude")
    public double latitude;

    @ColumnInfo(name = "longitude")
    public double longitude;

    @ColumnInfo(name = "language")
    public String language;

    @ColumnInfo(name = "name")
    public String name;

    public Place() {
    }

    @Ignore
    public Place(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Ignore
    public Place(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Ignore
    public Place(double latitude, double longitude, String language) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.language = language;
    }

    @Ignore
    public Place(String name, double latitude, double longitude, String language) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.language = language;
    }

    public void swap(Place target) {
        double latitude = this.latitude;
        this.latitude = target.latitude;
        target.latitude = latitude;

        double longitude = this.longitude;
        this.longitude = target.longitude;
        target.longitude = longitude;

        String language = this.language;
        this.language = target.language;
        target.language = language;

        String name = this.name;
        this.name = target.name;
        target.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                Double.doubleToLongBits(latitude),
                Double.doubleToLongBits(longitude),
                language
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Current current)) {
            return false;
        }

        return Double.compare(latitude, current.latitude) == 0
                && Double.compare(longitude, current.longitude) == 0
                && Objects.equals(language, current.language);
    }
}