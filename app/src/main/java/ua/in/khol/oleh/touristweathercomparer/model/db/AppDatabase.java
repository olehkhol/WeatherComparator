package ua.in.khol.oleh.touristweathercomparer.model.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ua.in.khol.oleh.touristweathercomparer.model.db.dao.CityDao;
import ua.in.khol.oleh.touristweathercomparer.model.db.dao.ForecastDao;
import ua.in.khol.oleh.touristweathercomparer.model.db.dao.ProviderDao;
import ua.in.khol.oleh.touristweathercomparer.model.db.dao.TitleDao;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.City;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Forecast;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Provider;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Title;

@Database(entities = {City.class, Forecast.class, Provider.class, Title.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CityDao getCityDao();

    public abstract ForecastDao getForecastDao();

    public abstract ProviderDao getProviderDao();

    public abstract TitleDao getTitleDao();
}
