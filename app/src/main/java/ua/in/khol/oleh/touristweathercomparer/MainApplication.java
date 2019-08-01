package ua.in.khol.oleh.touristweathercomparer;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import ua.in.khol.oleh.touristweathercomparer.dagger.AppComponent;
import ua.in.khol.oleh.touristweathercomparer.dagger.AppModule;
import ua.in.khol.oleh.touristweathercomparer.dagger.DaggerAppComponent;
import ua.in.khol.oleh.touristweathercomparer.dagger.RepositoryModule;

/**
 * Created by oleh.
 */

public class MainApplication extends Application {

    private RefWatcher mRefWatcher;
    private AppComponent mAppComponent;

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not updatePreferences your app in this process.
            return;
        }
        mRefWatcher = LeakCanary.install(this);
        // Normal app updatePreferences code...

        // Instantiating the components of dagger
        mAppComponent = DaggerAppComponent.builder()
                // For any module whose @Provides methods are all static,
                // the implementation doesn’t need an instance at all.
                .appModule(new AppModule(this))
                .repositoryModule(new RepositoryModule())
                .build();
    }

    public static RefWatcher getRefWatcher(Context context) {
        MainApplication mainApplication = (MainApplication) context.getApplicationContext();
        return mainApplication.mRefWatcher;
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
