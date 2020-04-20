package ua.in.khol.oleh.touristweathercomparer.model.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ua.in.khol.oleh.touristweathercomparer.model.db.dao.ForecastDao;
import ua.in.khol.oleh.touristweathercomparer.model.db.dao.PlaceDao;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Forecast;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Place;

@Database(entities = {Place.class, Forecast.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PlaceDao getPlaceDao();

    public abstract ForecastDao getForecastDao();
}
