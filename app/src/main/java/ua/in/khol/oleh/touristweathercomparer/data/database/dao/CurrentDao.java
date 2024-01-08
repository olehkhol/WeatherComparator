package ua.in.khol.oleh.touristweathercomparer.data.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Current;

@Dao
public interface CurrentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertCurrent(Current current);

    @Query("SELECT * FROM Current WHERE latitude = :latitude AND longitude = :longitude  AND language = :language  AND units = :units ORDER BY date DESC LIMIT 1")
    Maybe<Current> tryCurrent(double latitude, double longitude, String language, String units);
}
