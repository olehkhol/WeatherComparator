package ua.in.khol.oleh.touristweathercomparer.model.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import ua.in.khol.oleh.touristweathercomparer.model.db.data.Place;

@Dao
public interface PlaceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertPlace(Place place);

    @Query("SELECT id FROM Place WHERE latitude = :lat AND longitude = :lon LIMIT 1")
    long findPlaceIdByLatLon(double lat, double lon);

    @Query("SELECT * FROM Place WHERE latitude = :lat AND longitude = :lon AND language = :lang LIMIT 1")
    Place findByLatLon(double lat, double lon, String lang);
}
