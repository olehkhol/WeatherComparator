package ua.in.khol.oleh.touristweathercomparer.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(indices = {@Index(value = {"latitude", "longitude", "date", "language", "units"}, unique = true)})
public class Hourly {

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

    @ColumnInfo(name = "temp_min")
    public float tempMin;

    @ColumnInfo(name = "temp_max")
    public float tempMax;

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

    public Hourly() {
    }

    public long getId() {
        return id;
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
        if (!(obj instanceof Hourly hourly)) {
            return false;
        }

        return Double.compare(latitude, hourly.latitude) == 0
                && Double.compare(longitude, hourly.longitude) == 0
                && date == hourly.date
                && Objects.equals(language, hourly.language)
                && Objects.equals(units, hourly.units);
    }
}