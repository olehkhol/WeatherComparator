package ua.in.khol.oleh.touristweathercomparer.model.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PlaceDao {

    @Insert
    long insertPlace(Place place);

    @Query("SELECT * FROM Place")
    List<Place> loadPlaces();

    @Query("DELETE FROM Place WHERE id = :placeId")
    void deletePlace(int placeId);

    @Delete
    int deletePlace(Place place);
}
