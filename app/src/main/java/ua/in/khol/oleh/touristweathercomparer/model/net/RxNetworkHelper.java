package ua.in.khol.oleh.touristweathercomparer.model.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

public class RxNetworkHelper implements RxNetwork {
    private final Context mContext;

    public RxNetworkHelper(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public Observable<Boolean> observeInternetConnectivity() {
        final IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        final Observable<Boolean> observable
                = Observable.create(new RxBroadcastReceiver(mContext, filter))
                .map(intent -> checkConnectivityStatus(mContext));

        return observable.startWith(checkConnectivityStatus(mContext)).distinctUntilChanged();
    }

    @Override
    public boolean isConnected() {
        return checkConnectivityStatus(mContext);
    }

    private boolean checkConnectivityStatus(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return null != networkInfo && networkInfo.isConnected();
    }

    private static class RxBroadcastReceiver implements ObservableOnSubscribe<Intent>, Disposable {

        private final Context mContext;
        private final IntentFilter mFilter;
        private BroadcastReceiver mReceiver;
        private Emitter<? super Intent> mEmitter;

        RxBroadcastReceiver(Context context, IntentFilter filter) {
            mContext = context;
            mFilter = filter;
            mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    mEmitter.onNext(intent);
                }
            };
        }

        @Override
        public void subscribe(ObservableEmitter<Intent> emitter) {
            mEmitter = emitter;
            mContext.registerReceiver(mReceiver, mFilter);
        }

        @Override
        public void dispose() {
            mContext.unregisterReceiver(mReceiver);
            mReceiver = null;
        }

        @Override
        public boolean isDisposed() {
            return mReceiver == null;
        }
    }
}