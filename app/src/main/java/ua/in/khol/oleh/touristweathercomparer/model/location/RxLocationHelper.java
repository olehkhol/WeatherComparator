package ua.in.khol.oleh.touristweathercomparer.model.location;

import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.in.khol.oleh.touristweathercomparer.model.location.pojo.LocationModel;
import ua.in.khol.oleh.touristweathercomparer.model.location.pojo.Result;

public class RxLocationHelper implements LocationHelper {

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private SettingsClient mClient;

    public RxLocationHelper(Context context) {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        mClient = LocationServices.getSettingsClient(context);
    }

    public Single<Location> observeLocation(int accuracy, int power) {

        LocationRequest locationRequest = LocationRequest.create();

        return Single.create(emitter -> {

            locationRequest.setNumUpdates(1);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            mFusedLocationProviderClient.requestLocationUpdates(locationRequest,
                    new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            emitter.onSuccess(locationResult.getLastLocation());
                            mFusedLocationProviderClient.removeLocationUpdates(this);
                        }
                    }, null);
        });
    }

    @Override
    public Observable<String> observeLocationName(Location location, String language) {
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(new OkHttpClient.Builder().cache(null).build())
                .build();
        LocationDataService service = retrofit.create(LocationDataService.class);
        Observable<LocationModel> observable = service
                .observeLocationModel(location.getLatitude() + "," + location.getLongitude(),
                        language, LocationCityKey.getApiKey());

        return observable
                .map(locationData -> {
                    String name = "...";

                    if (locationData != null) {
                        List<Result> results = locationData.getResults();
                        boolean nameFound = false;

                        search_locality_name:
                        {
                            for (Result result : results) {
                                for (String type : result.getTypes()) {
                                    if ("locality".compareToIgnoreCase(type) == 0) {
                                        nameFound = true;
                                        name = result
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
                                        name = result.getFormattedAddress();
                                        break;
                                    }
                                }
                            }

                        }
                    }

                    return name;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    // TODO refactor this
    @Override
    public String getLocationName(double latitude, double longitude, String language) {
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(new OkHttpClient.Builder().cache(null).build())
                .build();
        LocationDataService service = retrofit.create(LocationDataService.class);
        Call<LocationModel> locationModelCall = service.getLocationModel(latitude + "," + longitude,
                language, LocationCityKey.getApiKey());

        String name = "...";
        try {
            LocationModel locationModel = locationModelCall.execute().body();

            if (locationModel != null) {
                List<Result> results = locationModel.getResults();
                boolean nameFound = false;

                search_locality_name:
                {
                    for (Result result : results) {
                        for (String type : result.getTypes()) {
                            if ("locality".compareToIgnoreCase(type) == 0) {
                                nameFound = true;
                                name = result
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
                                name = result.getFormattedAddress();
                                break;
                            }
                        }
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return name;
    }

    @Override
    public Single<Boolean> observeLocationUsable() {
        return Single.create(emitter -> {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            Task<LocationSettingsResponse> task = mClient.checkLocationSettings(builder.build());
            task.addOnSuccessListener(locationSettingsResponse -> {
                if (locationSettingsResponse != null) {
                    LocationSettingsStates states
                            = locationSettingsResponse.getLocationSettingsStates();
                    if (states != null)
                        emitter.onSuccess(states.isLocationUsable());
                }
            });
            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e != null);
                }
            });
        });
    }
}
