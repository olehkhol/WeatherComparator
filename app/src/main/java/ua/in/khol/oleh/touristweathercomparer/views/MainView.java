package ua.in.khol.oleh.touristweathercomparer.views;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import javax.inject.Inject;

import ua.in.khol.oleh.touristweathercomparer.BR;
import ua.in.khol.oleh.touristweathercomparer.MainApplication;
import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.contracts.MainContract;
import ua.in.khol.oleh.touristweathercomparer.databinding.ViewMainBinding;
import ua.in.khol.oleh.touristweathercomparer.helpers.MarketView;
import ua.in.khol.oleh.touristweathercomparer.model.Repository;
import ua.in.khol.oleh.touristweathercomparer.presenters.MainPresenter;
import ua.in.khol.oleh.touristweathercomparer.presenters.PresenterFactory;
import ua.in.khol.oleh.touristweathercomparer.presenters.PresenterLoader;
import ua.in.khol.oleh.touristweathercomparer.views.adapters.RecyclerAdapter;
import ua.in.khol.oleh.touristweathercomparer.views.adapters.UpperAdapter;
import ua.in.khol.oleh.touristweathercomparer.views.observables.City;
import ua.in.khol.oleh.touristweathercomparer.views.observables.Provider;
import ua.in.khol.oleh.touristweathercomparer.views.observables.Title;

public class MainView extends AppCompatActivity
        implements
        MainContract.View,
        LoaderManager.LoaderCallbacks<MainContract.Presenter>,
        NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainView.class.getSimpleName();
    private static final String SETTINGS_FRAGMENT_TAG = "SettingsFragment";
    private static final String SETTINGS_FRAGMENT_NAME = "Settings";
    private static final int LOCATION_PERMISSION_ID = 11;

    private boolean mHasPermissions = false;
    // UI variables
    private RecyclerView mUpperRecycler;
    private UpperAdapter mUpperAdapter;
    private RecyclerAdapter<Title> mLowerAdapter;
    // UI observables
    private ViewMainBinding mBinding;
    private City mCity = new City();
    // MVP variables
    private static final int LOADER_ID = 0;
    private MainContract.Presenter mMainPresenter;
    @Inject
    Repository mRepository;

    // -=-=-=-=-=-=-=-=[LIFECYCLE CALLBACKS]=-=-=-=-=-=-=-=-
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Dagger injection
        ((MainApplication) getApplication()).getAppComponent().inject(this);
        // Set content view
        mBinding = DataBindingUtil.setContentView(this, R.layout.view_main);
        initUI(mBinding);
        // Set UI variables
        setUI();
        // Request permissions
        requestPermissions();
    }

    @Override
    protected void onStop() {
        if (mMainPresenter != null)
            mMainPresenter.detachView();

        super.onStop();
    }
    // ----------------[LIFECYCLE CALLBACKS]----------------

    // -=-=-=-=-=-=-=-=[CONTRACT METHODS]=-=-=-=-=-=-=-=-
    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mMainPresenter = presenter;
        if (mHasPermissions)
            runEntireFunctionality();
    }

    @Override
    public void showCityName(String name) {
        mCity.setName(name);
    }

    @Override
    public void showLocation(double latitude, double longitude) {
        mCity.setLatitude(latitude);
        mCity.setLongitude(longitude);
    }

    @Override
    public void showError(String error) {
        Snackbar.make(mBinding.getRoot(), error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showMessage(String message) {
        Log.d(TAG, message);
    }

    @Override
    public void showTitles(List<Title> titles) {
        mLowerAdapter.update(titles);
        mLowerAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProviders(List<Provider> providers) {
        mUpperAdapter.update(providers);
        mUpperAdapter.notifyDataSetChanged();
    }

    @Override
    public void scrollTo(int position) {
        mUpperRecycler.getLayoutManager().scrollToPosition(position);
    }

    @Override
    public void updateUI() {
        overridePendingTransition(0, 0);
        recreate();
        overridePendingTransition(0, 0);
    }
    // ----------------[CONTRACT METHODS]----------------

    // -=-=-=-=-=-=-=-=[LOADERMANAGER CALLBACKS]=-=-=-=-=-=-=-=-
    @NonNull
    @Override
    public Loader<MainContract.Presenter> onCreateLoader(int id, @Nullable Bundle args) {
        return new PresenterLoader<>(this, getPresenterFactory());
    }

    private PresenterFactory<MainContract.Presenter> getPresenterFactory() {
        return () -> new MainPresenter(mRepository);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<MainContract.Presenter> loader,
                               MainContract.Presenter presenter) {
        setPresenter(presenter);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<MainContract.Presenter> loader) {
    }
    // ----------------[LOADERMANAGER CALLBACKS]----------------

    // -=-=-=-=-=-=-=-=[PERMISSIONS]=-=-=-=-=-=-=-=-

    /**
     * This method asks for location permissions and
     * starts presenter if the permissions are granted.
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
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        if (!shouldShowRequestPermissionRationale(permissions[i])) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle(getString(R.string.error));
                            builder.setMessage(getString(R.string.permission));
                            builder.setPositiveButton(getString(R.string.finish),
                                    (dialog, which) -> finish());
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            mHasPermissions = true;
                        }
                    }
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

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
            mMainPresenter.destroy();
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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
            SettingsView settingsFragment = (SettingsView) getSupportFragmentManager()
                    .findFragmentByTag(SETTINGS_FRAGMENT_TAG);
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (settingsFragment == null) {
                settingsFragment = new SettingsView();
                fragmentManager.beginTransaction()
                        .replace(R.id.content, settingsFragment, SETTINGS_FRAGMENT_TAG)
                        .addToBackStack(SETTINGS_FRAGMENT_NAME)
                        .commit();
            } else {
                fragmentManager.beginTransaction()
                        .remove(settingsFragment)
                        .commit();
                fragmentManager.popBackStack();
            }

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
                MarketView.open(this,
                        "ua.in.khol.oleh.touristweathercomparer");
                break;
            case R.id.nav_seo:
                MarketView.open(this,
                        "com.humanneeds.stackexchangeoffline");
                break;
            case R.id.nav_ujt:
                MarketView.open(this,
                        "com.humanneeds.upworkjavatest");
                break;
            case R.id.nav_b64:
                MarketView.open(this,
                        "com.humanneeds.base64fileencoderdecoder");
                break;
            case R.id.nav_afc:
                MarketView.open(this,
                        "com.humanneeds.aliensflashlight");
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    // ----------------[MENU CALLBACKS]----------------

    // -=-=-=-=-=-=-=-=[REGULAR METHODS]=-=-=-=-=-=-=-=-
    private void runEntireFunctionality() {
        if (mMainPresenter != null)
            mMainPresenter.attachView(this);
        else {
            // Create or get saved presenter
            loadPresenter();
        }
    }

    private void loadPresenter() {
        LoaderManager loaderManager = LoaderManager.getInstance(this);
        Loader<MainContract.Presenter> loader = loaderManager.getLoader(LOADER_ID);
        if (loader == null) {
            loaderManager.initLoader(LOADER_ID, null, this);
            loader = loaderManager.getLoader(LOADER_ID);
            if (loader != null && !loader.isReset()) {
                loaderManager.restartLoader(LOADER_ID, null, this);
            }
        } else {
            setPresenter(((PresenterLoader<MainContract.Presenter>) loader).getPresenter());
        }
    }

    // ----------------[REGULAR METHODS]----------------

    // -=-=-=-=-=-=-=-=[UI]=-=-=-=-=-=-=-=-
    private void initUI(ViewMainBinding binding) {
        binding.setCity(mCity);
        mUpperRecycler = binding.upperRecycler;
        mUpperRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        mUpperAdapter = new UpperAdapter();
        mUpperAdapter.setOnBannerClickListener(url -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });
        mUpperRecycler.setAdapter(mUpperAdapter);

        RecyclerView lowerRecycler = binding.lowerRecycler;
        lowerRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        mLowerAdapter = new RecyclerAdapter<>(R.layout.title_item, BR.title, null);
        mLowerAdapter.setOnItemClickListener((position, item)
                -> mMainPresenter.onProviderClicked(position));
        lowerRecycler.setAdapter(mLowerAdapter);
    }

    private void setUI() {
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
    // ----------------[UI]----------------

}
