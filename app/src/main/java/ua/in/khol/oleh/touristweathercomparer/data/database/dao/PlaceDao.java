package ua.in.khol.oleh.touristweathercomparer.data.database.dao;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Place;

@Dao
public interface PlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertPlace(Place place);

    @Query("SELECT * FROM Place ORDER BY id DESC")
    Observable<Place> observePlace();

    @Query("SELECT * FROM Place ORDER BY id DESC LIMIT :limit")
    Observable<List<Place>> observePlaces(int limit);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Completable updatePlaces(List<Place> places);

    @Delete
    Completable deletePlace(Place place);

    @Query("SELECT * FROM Place ORDER BY id DESC")
    DataSource.Factory<Integer, Place> placesById();
}