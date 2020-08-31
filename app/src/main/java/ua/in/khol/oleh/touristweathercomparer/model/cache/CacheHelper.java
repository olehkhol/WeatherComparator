package ua.in.khol.oleh.touristweathercomparer.model.cache;

import java.util.List;

import io.reactivex.Maybe;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Current;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Daily;

public interface CacheHelper {

    void putCurrents(long placeId, List<Current> currents, int time);

    Maybe<List<Current>> tryCurrents(long placeId, int time);

    void putDailies(long placeId, List<Daily> dailies, int time);

    Maybe<List<Daily>> tryDailies(long placeId, int time);
}
