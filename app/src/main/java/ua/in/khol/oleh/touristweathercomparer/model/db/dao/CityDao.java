package ua.in.khol.oleh.touristweathercomparer.model.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.City;

@Dao
public interface CityDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertCity(City city);

    @Query("SELECT * FROM City WHERE location_id = :id")
    City findByLocationId(long id);
}
