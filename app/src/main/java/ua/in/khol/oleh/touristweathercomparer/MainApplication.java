package ua.in.khol.oleh.touristweathercomparer;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;
import ua.in.khol.oleh.touristweathercomparer.di.AppModule;
import ua.in.khol.oleh.touristweathercomparer.di.DaggerAppComponent;

/**
 * Created by oleh.
 */

public class MainApplication extends Application implements HasAndroidInjector {

    @Inject
    DispatchingAndroidInjector<Object> mDispatchingAndroidInjector;

    @Override
    public AndroidInjector<Object> androidInjector() {
        return mDispatchingAndroidInjector;
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Instantiating the components of dagger
        DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build()
                .inject(this);
    }
}
