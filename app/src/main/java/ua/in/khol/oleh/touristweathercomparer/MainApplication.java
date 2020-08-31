package ua.in.khol.oleh.touristweathercomparer;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import androidx.multidex.MultiDex;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.conscrypt.Conscrypt;

import java.security.Security;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;
import timber.log.Timber;
import ua.in.khol.oleh.touristweathercomparer.di.AppModule;
import ua.in.khol.oleh.touristweathercomparer.di.DaggerAppComponent;

public class MainApplication extends Application
        implements HasAndroidInjector {
    @Inject
    DispatchingAndroidInjector<Object> mDispatchingAndroidInjector;

    @Override
    public AndroidInjector<Object> androidInjector() {
        return mDispatchingAndroidInjector;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        // Use Conscrypt security provider to enable TLS1.2
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1)
            Security.insertProviderAt(Conscrypt.newProviderBuilder().build(), 1);

        super.onCreate();

        // Install a DebugTree instance for Timber
        if (BuildConfig.DEBUG)
            Timber.plant(new Timber.DebugTree());

        // Instantiating the components of Dagger
        DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build()
                .inject(this);

        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }
}
