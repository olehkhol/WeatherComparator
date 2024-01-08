package ua.in.khol.oleh.touristweathercomparer.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(indices = {@Index(value = {"latitude", "longitude", "date", "language", "units"}, unique = true)})
public class Current {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "latitude")
    public double latitude;

    @ColumnInfo(name = "longitude")
    public double longitude;

    @ColumnInfo(name = "language")
    public String language;

    @ColumnInfo(name = "units")
    public String units;

    @ColumnInfo(name = "date")
    public int date;

    @ColumnInfo(name = "temp")
    public float temp;

    @ColumnInfo(name = "pressure")
    public float pressure;

    @ColumnInfo(name = "speed")
    public float speed;

    @ColumnInfo(name = "degree")
    public int degree;

    @ColumnInfo(name = "humidity")
    public int humidity;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "image")
    public String image;

    public Current() {
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                Double.doubleToLongBits(latitude),
                Double.doubleToLongBits(longitude),
                date,
                language,
                units
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Current current)) {
            return false;
        }

        return Double.compare(latitude, current.latitude) == 0
                && Double.compare(longitude, current.longitude) == 0
                && date == current.date
                && Objects.equals(language, current.language)
                && Objects.equals(units, current.units);
    }
}