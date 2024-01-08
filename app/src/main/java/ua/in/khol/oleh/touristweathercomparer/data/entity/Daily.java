package ua.in.khol.oleh.touristweathercomparer.data.entity;

import java.util.Objects;

public class Daily {

    public String language;
    public String units;
    public int date;
    public int timeMin;
    public int timeMax;
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
        if (!(obj instanceof Current current)) {
            return false;
        }

        return date == current.date
                && Objects.equals(language, current.language)
                && Objects.equals(units, current.units);
    }
}