package ua.in.khol.oleh.touristweathercomparer.model.cache;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Maybe;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Current;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Daily;

public class RxCacheHelper implements CacheHelper {
    private final static int TIMEOUT = 3600;

    private final Map<Long, List<Current>> mPlaceCurrentsMap;
    private final Map<Long, List<Daily>> mPlaceDailiesMap;
    private final Map<Long, Integer> mPlaceCurrentsTimestampMap;
    private final Map<Long, Integer> mPlaceDailiesTimestampMap;

    @SuppressLint("UseSparseArrays")
    public RxCacheHelper() {
        mPlaceDailiesMap = new HashMap<>();
        mPlaceCurrentsMap = new HashMap<>();
        mPlaceCurrentsTimestampMap = new HashMap<>();
        mPlaceDailiesTimestampMap = new HashMap<>();
    }

    @Override
    public void putCurrents(long placeId, List<Current> currents, int time) {
        mPlaceCurrentsTimestampMap.put(placeId, time);
        mPlaceCurrentsMap.put(placeId, currents);
    }

    @Override
    public Maybe<List<Current>> tryCurrents(long placeId, int time) {
        return Maybe.fromCallable(() -> {
            Integer timestamp = mPlaceCurrentsTimestampMap.get(placeId);
            if (timestamp != null && time - timestamp < TIMEOUT)
                return mPlaceCurrentsMap.get(placeId);

            return null;
        });
    }

    @SuppressLint("UseSparseArrays")
    @Override
    public void putDailies(long placeId, List<Daily> dailies, int time) {
        mPlaceDailiesTimestampMap.put(placeId, time);
        mPlaceDailiesMap.put(placeId, dailies);
    }

    @Override
    public Maybe<List<Daily>> tryDailies(long placeId, int time) {
        return Maybe.fromCallable(() -> {
            Integer timestamp = mPlaceDailiesTimestampMap.get(placeId);
            if (timestamp != null && time - timestamp < TIMEOUT)
                return mPlaceDailiesMap.get(placeId);

            return null;
        });
    }
}
