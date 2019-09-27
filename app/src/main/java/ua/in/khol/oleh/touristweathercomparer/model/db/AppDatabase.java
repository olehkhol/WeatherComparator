package ua.in.khol.oleh.touristweathercomparer.model.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ua.in.khol.oleh.touristweathercomparer.model.db.dao.CityDao;
import ua.in.khol.oleh.touristweathercomparer.model.db.dao.ForecastDao;
import ua.in.khol.oleh.touristweathercomparer.model.db.dao.PlaceDao;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.City;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Forecast;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Place;

@Database(entities = {Place.class, City.class, Forecast.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PlaceDao getPlaceDao();

    public abstract CityDao getCityDao();

    public abstract ForecastDao getForecastDao();
}
