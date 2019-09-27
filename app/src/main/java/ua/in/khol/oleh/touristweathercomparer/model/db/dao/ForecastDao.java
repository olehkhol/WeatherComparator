package ua.in.khol.oleh.touristweathercomparer.model.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Forecast;

@Dao
public interface ForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertForecast(Forecast forecast);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<Forecast> forecasts);

    @Query("SELECT * FROM Forecast WHERE place_id = :placeId")
    List<Forecast> queryByPlaceId(long placeId);

    @Query("SELECT id FROM Forecast WHERE provider_id = :providerId AND place_id = :placeId AND date = :date")
    long findForecastId(int providerId, long placeId, int date);
}
