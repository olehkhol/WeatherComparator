package ua.in.khol.oleh.touristweathercomparer.model.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Maybe;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Daily;

@Dao
public interface DailyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDailies(List<Daily> dailies);

    @Query("SELECT * FROM Daily WHERE place_id = :placeId AND date >= :date")
    Maybe<List<Daily>> tryDailies(long placeId, int date);

}
