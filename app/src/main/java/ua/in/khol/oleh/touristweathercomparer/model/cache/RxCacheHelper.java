package ua.in.khol.oleh.touristweathercomparer.model.cache;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ua.in.khol.oleh.touristweathercomparer.model.db.data.Forecast;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Place;
import ua.in.khol.oleh.touristweathercomparer.model.location.LatLon;

public class RxCacheHelper implements CacheHelper {
    private LatLon mLatLon;
    private final Set<Place> mPlaceSet;
    private final Map<Long, List<Forecast>> mPlaceCurrentsMap;
    private final Map<Long, List<Forecast>> mPlaceForecastsMap;

    @SuppressLint("UseSparseArrays")
    public RxCacheHelper() {
        mPlaceSet = new HashSet<>();
        mPlaceForecastsMap = new HashMap<>();
        mPlaceCurrentsMap = new HashMap<>();
    }

    @Override
    public void putLatLon(LatLon latLon) {
        mLatLon = new LatLon(latLon.getLat(), latLon.getLon());
    }

    @Override
    public LatLon getLatLon() {
        return mLatLon;
    }

    @Override
    public void putPlace(Place place) {
        mPlaceSet.add(place);
    }

    @Override
    public Place getPlace(double lat, double lon, String lang) {
        for (Place place : mPlaceSet)
            if (place.getLatitude() == lat && place.getLongitude() == lon
                    && place.getLanguage().equals(lang))
                return place;

        return null;
    }

    @Override
    public List<Forecast> getCurrents(long placeId, int date) {
        List<Forecast> currents = mPlaceCurrentsMap.get(placeId);
        if (currents != null)
            return filterWithDate(currents, date);

        return null;
    }

    @Override
    public void putCurrents(long placeId, List<Forecast> forecasts) {
        mPlaceCurrentsMap.put(placeId, forecasts);
    }

    @Override
    public List<Forecast> getDailies(long placeId, int date) {
        List<Forecast> forecasts = mPlaceForecastsMap.get(placeId);
        if (forecasts != null)
            return filterWithDate(forecasts, date);

        return null;

    }

    @SuppressLint("UseSparseArrays")
    @Override
    public void putDailies(long placeId, List<Forecast> forecasts) {
        mPlaceForecastsMap.put(placeId, forecasts);
    }

    private List<Forecast> filterWithDate(List<Forecast> forecasts, int date) {
        int size = forecasts.size();

        for (int i = 0; i < size; i++)
            if (forecasts.get(i).getDate() >= date)
                return forecasts.subList(i, size);

        return null;
    }
}
