package ua.in.khol.oleh.touristweathercomparer.model.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Place;

@Dao
public interface PlaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPlace(Place place);

    @Query("SELECT * FROM Place WHERE id=:id")
    Single<Place> seePlace(long id);

    @Query("SELECT * FROM Place WHERE latitude = :latitude AND longitude = :longitude AND language = :language LIMIT 1")
    Maybe<Place> tryPlace(double latitude, double longitude, String language);

    @Query("SELECT * FROM Place ORDER BY id DESC LIMIT :count")
    Observable<List<Place>> observeLatestPlaces(int count);

    @Query("SELECT * FROM Place ORDER BY id DESC LIMIT 1")
    Observable<Place> observeLatestPlace();
}
