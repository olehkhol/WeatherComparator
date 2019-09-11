package ua.in.khol.oleh.touristweathercomparer.model.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Location;

@Dao
public interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertLocation(Location location);

    @Query("SELECT * FROM Location WHERE latitude = :lat AND longitude = :lon")
    long findIdByLatLon(double lat, double lon);
}
