package ua.in.khol.oleh.touristweathercomparer.data.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Hourly;

@Dao
public interface HourlyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertHourlies(List<Hourly> hourlies);

    @Query("SELECT * FROM Hourly WHERE latitude = :latitude AND longitude = :longitude  AND language = :language  AND units = :units ORDER BY date ASC")
    Maybe<List<Hourly>> tryHourlies(double latitude, double longitude, String language, String units);
}
