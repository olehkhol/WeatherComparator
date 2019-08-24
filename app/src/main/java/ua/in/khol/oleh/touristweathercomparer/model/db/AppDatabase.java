package ua.in.khol.oleh.touristweathercomparer.model.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Place.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PlaceDao getPlaceDao();
}
