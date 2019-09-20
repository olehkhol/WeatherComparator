package ua.in.khol.oleh.touristweathercomparer.model.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Title;

@Dao
public interface TitleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTitle(Title title);

    @Query("SELECT * FROM Title WHERE city_id = :id ORDER BY id ASC")
    List<Title> queryAll(long id);
}