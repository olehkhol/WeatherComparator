package ua.in.khol.oleh.touristweathercomparer.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ua.in.khol.oleh.touristweathercomparer.data.database.dao.CurrentDao;
import ua.in.khol.oleh.touristweathercomparer.data.database.dao.HourlyDao;
import ua.in.khol.oleh.touristweathercomparer.data.database.dao.PlaceDao;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Current;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Hourly;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Place;

@Database(entities = {Place.class, Current.class, Hourly.class}, version = 3)
public abstract class ApplicationDatabase extends RoomDatabase {

    public abstract PlaceDao placeDao();

    public abstract CurrentDao currentDao();

    public abstract HourlyDao hourlyDao();
}