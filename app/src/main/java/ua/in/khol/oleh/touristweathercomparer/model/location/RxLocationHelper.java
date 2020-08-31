package ua.in.khol.oleh.touristweathercomparer.model.location;

import android.content.Context;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import ua.in.khol.oleh.touristweathercomparer.model.Helper;
import ua.in.khol.oleh.touristweathercomparer.model.location.android.RxAndroidLocation;
import ua.in.khol.oleh.touristweathercomparer.model.location.google.RxGoogleLocation;

public class RxLocationHelper implements RxLocation, Helper {
    public static final int ANDROID_LOCATION = 0;
    public static final int GOOGLE_LOCATION = 1;

    private final Context mContext;
    private RxLocation mRxLocation;

    public RxLocationHelper(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public void setup(int source) {
        switch (source) {
            case GOOGLE_LOCATION:
                mRxLocation = new RxGoogleLocation(mContext);
                break;

            case ANDROID_LOCATION:
            default:
                mRxLocation = new RxAndroidLocation(mContext);
                break;
        }
    }

    @Override
    public Single<LatLon> seeLocation() {
        return mRxLocation.seeLocation();
    }

    @Override
    public Observable<Boolean> observeUsability() {
        return mRxLocation.observeUsability();
    }

    @Override
    public Maybe<LatLon> tryLatLon() {
        return mRxLocation.tryLatLon();
    }

}
