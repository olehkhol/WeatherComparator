package ua.in.khol.oleh.touristweathercomparer.model.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ua.in.khol.oleh.touristweathercomparer.model.db.dao.CurrentDao;
import ua.in.khol.oleh.touristweathercomparer.model.db.dao.DailyDao;
import ua.in.khol.oleh.touristweathercomparer.model.db.dao.PlaceDao;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Current;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Daily;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Place;

@Database(entities = {Place.class, Daily.class, Current.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PlaceDao getPlaceDao();

    public abstract DailyDao getDailyDao();

    public abstract CurrentDao getCurrentDao();
}
