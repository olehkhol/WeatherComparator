package ua.in.khol.oleh.touristweathercomparer.model.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ua.in.khol.oleh.touristweathercomparer.model.db.data.Forecast;

@Dao
public interface ForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertForecast(Forecast forecast);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertForecasts(List<Forecast> forecasts);

    @Query("SELECT * FROM Forecast WHERE place_id = :placeId AND date >= :date")
    List<Forecast> queryByPlaceIdDate(long placeId, int date);

    @Query("SELECT * FROM Forecast WHERE place_id = :placeId AND date >= :date AND current = :current")
    List<Forecast> queryByPlaceFromDate(long placeId, int date, boolean current);

    @Query("SELECT * FROM Forecast WHERE place_id = :placeId AND date >= :date AND current = :current")
    List<Forecast> queryFromDate(long placeId, int date, boolean current);

    @Query("SELECT * FROM Forecast WHERE provider_id = :providerId AND place_id = :placeId AND date >= :date AND current = :current")
    List<Forecast> queryFromDate(long providerId, long placeId, int date, boolean current);

    @Query("SELECT id FROM Forecast WHERE provider_id = :providerId AND place_id = :placeId AND date = :date")
    long findForecastId(long providerId, long placeId, int date);
}
