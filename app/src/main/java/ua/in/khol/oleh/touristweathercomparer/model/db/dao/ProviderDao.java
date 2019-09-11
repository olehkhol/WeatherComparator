package ua.in.khol.oleh.touristweathercomparer.model.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Provider;

@Dao
public interface ProviderDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertProvider(Provider provider);
}
