package ua.in.khol.oleh.touristweathercomparer.views;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.databinding.ViewMainBinding;
import ua.in.khol.oleh.touristweathercomparer.di.ViewModelProviderFactory;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.MainViewModel;

public class MainView extends AppCompatActivity
        implements ViewBinding<ViewMainBinding>,
        NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String PREFERENCES_TAG = SettingsView.class.getName();
    private static final String ALERT_TAG = AlertView.class.getName();
    private static final int LOCATION_PERMISSION_ID = 17;

    private boolean mHasPermissions = false;
    // UI variables
    private MainViewModel mMainViewModel;
    private DrawerLayout mDrawerLayout;

    @Inject
    ViewModelProviderFactory mViewModelProviderFactory;

    // -=-=-=-=-=-=-=-=[LIFECYCLE CALLBACKS]=-=-=-=-=-=-=-=-
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        mMainViewModel = new ViewModelProvider(this, mViewModelProviderFactory)
                .get(MainViewModel.class);
        // TODO[40] try find a better place to change language before .onCreate according to MVVM
        updateLocale(mMainViewModel.getSettings().get().getLanguageIndex());
        adjustFontScale();

        // Init
        super.onCreate(savedInstanceState);
        ViewMainBinding binding = DataBindingUtil.setContentView(this, R.layout.view_main);
        initBinding(binding);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Observe events
        mMainViewModel.getDoRecreate().observe(this, asked -> {
            if (asked)
                reload();
        });
        mMainViewModel.getAskForInternet().observe(this, asked -> {
            if (asked)
                showInternetAlertDialog();
            else
                showInternetAlertToast();
        });
        mMainViewModel.getAskForLocation().observe(this, asked -> {
            if (asked)
                showLocationAlertDialog();
        });

        requestPermissions();
    }
    // ----------------[LIFECYCLE CALLBACKS]----------------

    // -=-=-=-=-=-=-=-=[PERMISSIONS]=-=-=-=-=-=-=-=-
    private void requestPermissions() {
        if (ContextCompat
                .checkSelfPermission(
                        this,
                        "android.permission.ACCESS_FINE_LOCATION") == 0
                &&
                ContextCompat.checkSelfPermission(
                        this,
                        "android.permission.ACCESS_COARSE_LOCATION") == 0) {

            // Permissions granted
            mHasPermissions = true;
            // TODO try find a better place to refresh data according to MVVM
            refreshModelView();
        } else
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            "android.permission.ACCESS_FINE_LOCATION",
                            "android.permission.ACCESS_COARSE_LOCATION"},
                    LOCATION_PERMISSION_ID);
    }
    // ----------------[PERMISSIONS]----------------

    // -=-=-=-=-=-=-=-=[REGULAR METHODS]=-=-=-=-=-=-=-=-

    private void updateLocale(int index) {
        Locale locale = new Locale(getResources().getStringArray(R.array.languages_values)[index]);
        Locale.setDefault(locale);
        Configuration config = getResources().getConfiguration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private void adjustFontScale() {
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        if (configuration.fontScale > 1f) {
            configuration.fontScale = 1f;
            DisplayMetrics metrics = resources.getDisplayMetrics();
            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            metrics.scaledDensity = configuration.fontScale * metrics.density;
            resources.updateConfiguration(configuration, metrics);
            //applyOverrideConfiguration(configuration);
        }
    }
    // ----------------[REGULAR METHODS]----------------

    // -=-=-=-=-=-=-=-=[UI]=-=-=-=-=-=-=-=-
    @Override
    public void initBinding(ViewMainBinding binding) {
        binding.setMainViewModel(mMainViewModel);

        Toolbar toolbar = binding.appBar.toolbar;
        setSupportActionBar(toolbar);

        NavigationView navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        mDrawerLayout = binding.drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        BottomNavigationView bottomNavView = binding.appBar.content.bottomNavView;
        bottomNavView.setItemIconTintList(null);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_map, R.id.navigation_info)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavView, navController);
    }

    private void refreshModelView() {
        mMainViewModel.refresh();
    }

    private void reload() {
        new Handler().post(() -> {
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            overridePendingTransition(0, 0);
            finish();

            overridePendingTransition(0, 0);
            startActivity(intent);
        });
    }

    private void showInternetAlertDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AlertView alertView = (AlertView) fragmentManager
                .findFragmentByTag(ALERT_TAG);
        SettingsView preferencesFragment = (SettingsView) fragmentManager
                .findFragmentByTag(PREFERENCES_TAG);
        if (alertView == null && preferencesFragment == null) {
            alertView = AlertView.newInstance(getString(R.string.alert_wireless_title),
                    getString(R.string.alert_wireless_message),
                    Settings.ACTION_WIRELESS_SETTINGS);
            alertView.show(fragmentManager, ALERT_TAG);
        }
    }

    private void showInternetAlertToast() {
        Toast.makeText(this, getString(R.string.alert_wireless_title), Toast.LENGTH_LONG).show();
    }

    private void showLocationAlertDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AlertView alertView = (AlertView) fragmentManager
                .findFragmentByTag(ALERT_TAG);
        SettingsView preferencesFragment = (SettingsView) fragmentManager
                .findFragmentByTag(PREFERENCES_TAG);
        if (alertView == null && preferencesFragment == null) {
            alertView = AlertView.newInstance(getString(R.string.alert_location_title),
                    getString(R.string.alert_location_message),
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            alertView.show(fragmentManager, ALERT_TAG);
        }
    }

    private void showFinishAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.error));
        builder.setMessage(getString(R.string.permission));
        builder.setPositiveButton(getString(R.string.finish),
                (dialog, which) -> finish());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showPreferencesDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        SettingsView settingsView = (SettingsView) fragmentManager
                .findFragmentByTag(PREFERENCES_TAG);
        AlertView alertView = (AlertView) fragmentManager
                .findFragmentByTag(ALERT_TAG);

        if (alertView == null && settingsView == null) {
            settingsView = SettingsView.newInstance();
            settingsView.show(fragmentManager, PREFERENCES_TAG);
        }
    }

    private void shareApplicationName() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details"
                + "?id=ua.in.khol.oleh.touristweathercomparer");
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent,
                getResources().getString(R.string.share));
        startActivity(shareIntent);
    }

    /**
     * Opens application page in Google Play
     **/
    private void open(AppCompatActivity activity, String appPackageName) {
        try {
            activity.startActivity(
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + appPackageName)));
        } catch (ActivityNotFoundException e) {
            activity.startActivity(
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id="
                                    + appPackageName)));
        }
    }
    // ----------------[UI]----------------

    // -=-=-=-=-=-=-=-=[ACTIVITY CALLBACKS]=-=-=-=-=-=-=-=-
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_ID) {
            for (int i = 0; i < permissions.length; i++)
                if (grantResults[i] == PackageManager.PERMISSION_DENIED)
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
                        if (!shouldShowRequestPermissionRationale(permissions[i]))
                            showFinishAlertDialog();
        } else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (permissions.length > 0 && !mHasPermissions) {
            // Start request again because we really need this permissions to work
            requestPermissions();
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    // ----------------[ACTIVITY CALLBACKS]----------------

    // -=-=-=-=-=-=-=-=[MENU CALLBACKS]=-=-=-=-=-=-=-=-
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_item_settings) {
            showPreferencesDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_rate:
                open(this, "ua.in.khol.oleh.touristweathercomparer");
                break;
            case R.id.nav_share:
                shareApplicationName();
                break;
            case R.id.nav_seo:
                open(this, "com.humanneeds.stackexchangeoffline");
                break;
            case R.id.nav_ujt:
                open(this, "com.humanneeds.upworkjavatest");
                break;
            case R.id.nav_b64:
                open(this, "com.humanneeds.base64fileencoderdecoder");
                break;
            case R.id.nav_afc:
                open(this, "com.humanneeds.aliensflashlight");
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    // ----------------[MENU CALLBACKS]----------------

}
