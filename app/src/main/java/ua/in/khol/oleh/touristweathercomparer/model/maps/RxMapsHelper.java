package ua.in.khol.oleh.touristweathercomparer.model.maps;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.in.khol.oleh.touristweathercomparer.model.db.data.Place;
import ua.in.khol.oleh.touristweathercomparer.model.maps.pojo.GeocodingModel;
import ua.in.khol.oleh.touristweathercomparer.model.maps.pojo.Result;
import ua.in.khol.oleh.touristweathercomparer.model.maps.pojo.TimeZoneModel;

public class RxMapsHelper implements MapsHelper {
    private final MapsService mService;

    public RxMapsHelper() {
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(createOkHttpClient())
                .build();
        mService = retrofit.create(MapsService.class);
    }

    @Override
    public int getTimeZoneOffset(double latitude, double longitude, long timestamp) {
        Call<TimeZoneModel> timeZoneModelCall = mService
                .getTimeZoneModel(latitude + "," + longitude, String.valueOf(timestamp),
                        TimeZoneAuth.getTimeZoneApiKey());

        try {
            TimeZoneModel timeZoneModel = timeZoneModelCall.execute().body();
            if (timeZoneModel != null) {
                Integer rawOffset = timeZoneModel.getRawOffset();
                if (rawOffset != null)
                    return rawOffset;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public String getLocationName(double lat, double lon, String language) {
        Call<GeocodingModel> locationModelCall = mService
                .getLocationModel(lat + "," + lon, language,
                        GeocodingAuth.getGeocodingApiKey());
        String name = "";

        try {
            GeocodingModel geocodingModel = locationModelCall.execute().body();
            if (geocodingModel != null) {
                List<Result> results = geocodingModel.getResults();
                boolean nameFound = false;
                search_locality_name:
                {
                    for (Result result : results)
                        for (String type : result.getTypes())
                            if ("locality".compareToIgnoreCase(type) == 0) {
                                nameFound = true;
                                name = result
                                        .getAddressComponents().get(0).getShortName();
                                break search_locality_name;
                            }
                }
                if (!nameFound)
                    for (Result result : results)
                        for (String type : result.getTypes())
                            if ("administrative_area_level_2".compareToIgnoreCase(type) == 0) {
                                name = result.getFormattedAddress();
                                break;
                            }

//                if (!nameFound)
//                    for (Result result : results)
//                        for (String type : result.getTypes())
//                            if ("route".compareToIgnoreCase(type) == 0) {
//                                String[] names = result.getFormattedAddress().trim().split(",");
//                                name = names[1];
//                                break;
//                            }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return name;
    }

    @Override
    public Maybe<Place> tryPlace(double latitude, double longitude, String language) {
        return Maybe.fromCallable(() -> {
            String name = getLocationName(latitude, longitude, language);
            if (name.isEmpty())
                return null;

            int offset = getTimeZoneOffset(latitude, longitude,
                    new DateTime().getMillis() / 1000);

            return new Place(name, latitude, longitude, language, offset);
        });
    }

    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        return clientBuilder
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60 / 2, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .cache(null)
                .build();
    }
}
