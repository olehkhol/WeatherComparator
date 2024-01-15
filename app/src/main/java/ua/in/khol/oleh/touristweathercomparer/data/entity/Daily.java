package ua.in.khol.oleh.touristweathercomparer.data.entity;

import java.util.Objects;

public class Daily {

    public String language;
    public String units;
    public int date;
    public float tempMin;
    public float tempMax;
    public float pressure;
    public float speed;
    public int degree;
    public int humidity;

    @Override
    public int hashCode() {
        return Objects.hash(
                date,
                language,
                units
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Daily daily)) {
            return false;
        }

        return date == daily.date
                && Objects.equals(language, daily.language)
                && Objects.equals(units, daily.units);
    }
}