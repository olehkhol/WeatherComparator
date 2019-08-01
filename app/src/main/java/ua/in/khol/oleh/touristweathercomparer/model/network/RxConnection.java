package ua.in.khol.oleh.touristweathercomparer.model.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxConnection {

    private final ConnectivityManager mConnectivityManager;
    private final Context mContext;
    private final IntentFilter mFilter;
    private final BroadcastReceiver mBroadcastReceiver;
    private PublishSubject<Boolean> mPublishSubject;

    public RxConnection(Context context) {
        mContext = context;
        mConnectivityManager = (ConnectivityManager) mContext.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        mPublishSubject = PublishSubject.create();
        mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                if (isConnected) {
                    mPublishSubject.onNext(true);
                    mPublishSubject.onComplete();
                }
            }
        };
    }

    public Observable<Boolean> getStatus() {
        return Observable.create(emitter -> {
            NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

            emitter.onNext(isConnected);
        });
    }

    public Observable<Boolean> getStatusObservable() {
        mContext.registerReceiver(mBroadcastReceiver, mFilter);
        return mPublishSubject.serialize();
    }
}
