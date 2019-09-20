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

    @Query("SELECT * FROM City WHERE latitude = :lat AND longitude = :lon LIMIT 1")
    City findCityByLatLon(double lat, double lon);

    @Query("SELECT name FROM City WHERE latitude = :lat AND longitude = :lon LIMIT 1")
    String findCityNameByLatLon(double lat, double lon);

    @Query("SELECT id FROM City WHERE latitude = :lat AND longitude = :lon LIMIT 1")
    long findCityIdByLatLon(double lat, double lon);
}
