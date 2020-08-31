package ua.in.khol.oleh.touristweathercomparer.model.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Maybe;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Current;

@Dao
public interface CurrentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCurrents(List<Current> dailies);

    @Query("SELECT * FROM Current WHERE place_id = :placeId AND time >= :time")
    Maybe<List<Current>> tryCurrents(long placeId, int time);

}
