package ua.in.khol.oleh.touristweathercomparer.views;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import ua.in.khol.oleh.touristweathercomparer.BR;
import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.databinding.ViewMainBinding;
import ua.in.khol.oleh.touristweathercomparer.di.ViewModelProviderFactory;
import ua.in.khol.oleh.touristweathercomparer.utils.MarketView;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.MainViewModel;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Title;
import ua.in.khol.oleh.touristweathercomparer.views.adapters.RecyclerAdapter;
import ua.in.khol.oleh.touristweathercomparer.views.adapters.UpperAdapter;

public class MainView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String SETTINGS_TAG = SettingsView.class.getName();
    private static final String ALERT_TAG = AlertView.class.getName();
    private static final int LOCATION_PERMISSION_ID = 11;

    private boolean mHasPermissions = false;
    // UI variables
    private MainViewModel mMainViewModel;

    @Inject
    ViewModelProviderFactory mViewModelProviderFactory;
    private RecyclerView mUpperRecycler;
    private RecyclerView mLowerRecycler;
    private UpperAdapter mUpperAdapter;

    // -=-=-=-=-=-=-=-=[LIFECYCLE CALLBACKS]=-=-=-=-=-=-=-=-
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Dagger injection
        AndroidInjection.inject(this);
        // TODO Dagger this peace of code
        mMainViewModel = new ViewModelProvider(this, mViewModelProviderFactory)
                .get(MainViewModel.class);
        mMainViewModel.start();
        super.onCreate(savedInstanceState);
        // Init UI and Binding
        ViewMainBinding binding = DataBindingUtil.setContentView(this, R.layout.view_main);
        binding.setMainViewModel(mMainViewModel);
        initBindings(binding);
        initUI();
        // Observe live data
        mMainViewModel.getDoRecreate().observe(this, aBoolean -> {
            if (aBoolean) {
                mMainViewModel.setDoRecreate(false);
                overridePendingTransition(0, 0);
                recreate();
            }
        });
        mMainViewModel.getAskForInternetSoftly().observe(this, asked -> {
            if (asked) {
                mMainViewModel.setAskForInternetSoftly(false);
                Snackbar.make(binding.drawerLayout, R.string.ask_for_intenet, Snackbar.LENGTH_LONG)
                        .show();
            }
        });
        mMainViewModel.getAskForInternet().observe(this, asked -> {
            if (asked) {
                mMainViewModel.setAskForInternet(false);
                showInternetAlertDialog();
            }
        });
        mMainViewModel.getAskForLocation().observe(this, asked -> {
            if (asked) {
                mMainViewModel.setAskForLocation(false);
                showLocationAlertDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Request permissions
        requestPermissions();
    }

    // ----------------[LIFECYCLE CALLBACKS]----------------

    // -=-=-=-=-=-=-=-=[PERMISSIONS]=-=-=-=-=-=-=-=-

    /**
     * This method asks for location permissions and
     * starts ipresenter if the permissions are granted.
     */
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
            runEntireFunctionality();
        } else
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            "android.permission.ACCESS_FINE_LOCATION",
                            "android.permission.ACCESS_COARSE_LOCATION"},
                    LOCATION_PERMISSION_ID);
    }
    // ----------------[PERMISSIONS]----------------

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

        if (!mHasPermissions) {
            // Start request again because we really need this permissions to work
            requestPermissions();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        mUpperAdapter.setOnBannerClickListener(null);
        mUpperRecycler.setAdapter(null);
        mLowerRecycler.setAdapter(null);
        super.onDestroy();
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
            showSettingsFragment();
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
                MarketView.open(this, "ua.in.khol.oleh.touristweathercomparer");
                break;
            case R.id.nav_share:
                shareApplicationName();
                break;
            case R.id.nav_seo:
                MarketView.open(this, "com.humanneeds.stackexchangeoffline");
                break;
            case R.id.nav_ujt:
                MarketView.open(this, "com.humanneeds.upworkjavatest");
                break;
            case R.id.nav_b64:
                MarketView.open(this, "com.humanneeds.base64fileencoderdecoder");
                break;
            case R.id.nav_afc:
                MarketView.open(this, "com.humanneeds.aliensflashlight");
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    // ----------------[MENU CALLBACKS]----------------

    // -=-=-=-=-=-=-=-=[REGULAR METHODS]=-=-=-=-=-=-=-=-
    private void runEntireFunctionality() {
        mMainViewModel.processData();
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

    private void showSettingsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        SettingsView settingsFragment = (SettingsView) fragmentManager
                .findFragmentByTag(SETTINGS_TAG);
        if (settingsFragment == null) {
            settingsFragment = new SettingsView();
            fragmentManager.beginTransaction()
                    .replace(R.id.content, settingsFragment, SETTINGS_TAG)
                    .addToBackStack(null)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .remove(settingsFragment)
                    .commit();
            fragmentManager.popBackStack();
        }
    }

    private void showLocationAlertDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AlertView alertView = (AlertView) fragmentManager
                .findFragmentByTag(ALERT_TAG);
        SettingsView settingsFragment = (SettingsView) fragmentManager
                .findFragmentByTag(SETTINGS_TAG);
        if (alertView == null && settingsFragment == null) {
            alertView = AlertView.newInstance(getString(R.string.alert_location_title),
                    getString(R.string.alert_location_message),
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            alertView.show(fragmentManager, ALERT_TAG);
        }
    }

    private void showInternetAlertDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AlertView alertView = (AlertView) fragmentManager
                .findFragmentByTag(ALERT_TAG);
        SettingsView settingsFragment = (SettingsView) fragmentManager
                .findFragmentByTag(SETTINGS_TAG);
        if (alertView == null && settingsFragment == null) {
            alertView = AlertView.newInstance(getString(R.string.alert_wireless_title),
                    getString(R.string.alert_wireless_message),
                    Settings.ACTION_WIRELESS_SETTINGS);
            alertView.show(fragmentManager, ALERT_TAG);
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
    // ----------------[REGULAR METHODS]----------------

    // -=-=-=-=-=-=-=-=[UI]=-=-=-=-=-=-=-=-
    private void initUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initBindings(ViewMainBinding binding) {
        mUpperRecycler = binding.content.upperRecycler;
        mUpperRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        mUpperAdapter = new UpperAdapter();
        mUpperAdapter.setOnBannerClickListener(url -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            MainView.this.startActivity(intent);
        });
        mUpperRecycler.setAdapter(mUpperAdapter);

        mLowerRecycler = binding.content.lowerRecycler;
        mLowerRecycler.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.HORIZONTAL, false));
        RecyclerAdapter<Title> lowerAdapter
                = new RecyclerAdapter<>(R.layout.title_item, BR.title, null);
        lowerAdapter.setOnItemClickListener((position, item)
                -> mUpperRecycler.getLayoutManager().scrollToPosition(position));
        mLowerRecycler.setAdapter(lowerAdapter);
    }
    // ----------------[UI]----------------
}
