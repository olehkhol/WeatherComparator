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
    void insertAll(List<Forecast> forecasts);

    @Query("SELECT * FROM Forecast")
    List<Forecast> queryAll();
}
