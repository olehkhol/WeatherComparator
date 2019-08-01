package ua.in.khol.oleh.touristweathercomparer.model.location;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.in.khol.oleh.touristweathercomparer.model.location.pojo.LocationData;
import ua.in.khol.oleh.touristweathercomparer.model.location.pojo.Result;


/**
 * Created by Oleh Kholiavchuk.
 */
public class RxLocationCity {
    private static final String TAG = RxLocationCity.class.getSimpleName();

    /**
     * Find the city name according to the given location.
     *
     * @param latitude  - double
     * @param longitude - double
     */
    public static Observable<String> getCityObservable(double latitude, double longitude,
                                                       String language) {

        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(new OkHttpClient.Builder().cache(null).build())
                .build();
        RxLocationCityService service = retrofit.create(RxLocationCityService.class);
        Observable<LocationData> observable = service
                .getCityName(latitude + "," + longitude, language,
                        LocationCityKey.getApiKey());

        return observable
                .map(locationData -> {
                    String cityName = null;
                    boolean nameFound = false;

                    List<Result> results;
                    if (locationData != null) {
                        results = locationData.getResults();
                        search_locality_name:
                        {
                            for (Result result : results) {
                                for (String type : result.getTypes()) {
                                    if ("locality".compareToIgnoreCase(type) == 0) {
                                        nameFound = true;
                                        cityName = result
                                                .getAddressComponents().get(0).getShortName();
                                        break search_locality_name;
                                    }
                                }
                            }
                        }


                        if (!nameFound) {
                            for (Result result : results) {
                                for (String type : result.getTypes()) {
                                    if ("administrative_area_level_2"
                                            .compareToIgnoreCase(type) == 0) {
                                        cityName = result.getFormattedAddress();
                                        break;
                                    }
                                }
                            }

                        }
                    }

                    return cityName;
                });
    }

}
