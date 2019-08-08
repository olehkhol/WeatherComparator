package ua.in.khol.oleh.touristweathercomparer.views;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import javax.inject.Inject;

import ua.in.khol.oleh.touristweathercomparer.BR;
import ua.in.khol.oleh.touristweathercomparer.MainApplication;
import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.databinding.ViewMainBinding;
import ua.in.khol.oleh.touristweathercomparer.helpers.MarketView;
import ua.in.khol.oleh.touristweathercomparer.model.Repository;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.MainViewModel;
import ua.in.khol.oleh.touristweathercomparer.views.adapters.RecyclerAdapter;
import ua.in.khol.oleh.touristweathercomparer.views.adapters.UpperAdapter;
import ua.in.khol.oleh.touristweathercomparer.views.observables.City;
import ua.in.khol.oleh.touristweathercomparer.views.observables.Title;

public class MainView extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener {

    private static final String SETTINGS_FRAGMENT_TAG = "SettingsFragment";
    private static final String SETTINGS_FRAGMENT_NAME = "Settings";
    private static final int LOCATION_PERMISSION_ID = 11;

    private boolean mHasPermissions = false;
    // UI variables
    private RecyclerView mUpperRecycler;
    private UpperAdapter mUpperAdapter;
    private RecyclerAdapter<Title> mLowerAdapter;
    private City mCity = new City();

    @Inject
    Repository mRepository;

    // -=-=-=-=-=-=-=-=[LIFECYCLE CALLBACKS]=-=-=-=-=-=-=-=-
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Dagger injection
        ((MainApplication) getApplication()).getAppComponent().inject(this);
        // Set content view
        // UI observables
        ViewMainBinding binding = DataBindingUtil.setContentView(this, R.layout.view_main);
        initUI(binding);
        // Set UI variables
        setUI();
        // Request permissions
        requestPermissions();
    }
    // ----------------[LIFECYCLE CALLBACKS]----------------

    // -=-=-=-=-=-=-=-=[CONTRACT METHODS]=-=-=-=-=-=-=-=-
    public void scrollTo(int position) {
        mUpperRecycler.getLayoutManager().scrollToPosition(position);
    }
    // ----------------[CONTRACT METHODS]----------------

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
        // MVVM variables
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.setRepository(mRepository);
        viewModel.getCityName().observe(this, name -> mCity.setName(name));
        viewModel.getCityLocation().observe(this, cityLocation -> {
            mCity.setLatitude(cityLocation.getLatitude());
            mCity.setLongitude(cityLocation.getLongitude());
        });
        viewModel.getTitlesObservable().observe(this,
                titles -> mLowerAdapter.update(titles));
        viewModel.getProvidersObservable().observe(this,
                providers -> mUpperAdapter.update(providers));
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
        mLowerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener<Title>() {
            @Override
            public void onItemClick(int position, Title item) {
                // TODO
                // mMainPresenter.onProviderClicked(position);
            }
        });
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
