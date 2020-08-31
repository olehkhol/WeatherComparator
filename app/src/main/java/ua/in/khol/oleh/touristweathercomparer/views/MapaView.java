package ua.in.khol.oleh.touristweathercomparer.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.databinding.ViewMapaBinding;
import ua.in.khol.oleh.touristweathercomparer.databinding.ViewMarkerBinding;
import ua.in.khol.oleh.touristweathercomparer.model.settings.Settings;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.MapaViewModel;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.ViewModelProviderFactory;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Average;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.City;
import ua.in.khol.oleh.touristweathercomparer.views.adapters.MiniAverageAdapter;

public class MapaView extends Fragment
        implements ViewBinding<ViewMapaBinding>,
        OnMapReadyCallback, GoogleMap.OnCameraMoveListener, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {
    private static final int DAYS_TO_DISPLAY_ON_MAP = 4;
    private static final String MAP_ZOOM = "MAP_ZOOM";
    private static final float DEFAULT_ZOOM = 12f;
    private static final LocationSource STUB_LOCATION_SOURCE = new LocationSource() {
        @Override
        public void activate(OnLocationChangedListener onLocationChangedListener) {
        }

        @Override
        public void deactivate() {
        }
    };

    @Inject
    ViewModelProviderFactory mFactory;

    private ViewMapaBinding mBinding;
    private MapaViewModel mViewModel;
    private InfoWindowView mInfoWindowView;
    private GoogleMap mMap;
    private Marker mMarker;
    private float mZoom;

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);

        super.onAttach(context);

        mViewModel = new ViewModelProvider(this, mFactory).get(MapaViewModel.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mZoom = savedInstanceState == null
                ? DEFAULT_ZOOM : savedInstanceState.getFloat(MAP_ZOOM, DEFAULT_ZOOM);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = ViewMapaBinding.inflate(inflater, container, false);
        mBinding.setViewModel(mViewModel);
        initBinding(mBinding);

        return mBinding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putFloat(MAP_ZOOM, mZoom);
    }

    @Override
    public void onDestroy() {
        mViewModel.getAverages().removeObservers(this);
        mViewModel.getCity().removeObservers(this);

        super.onDestroy();
    }

    @Override
    public void initBinding(ViewMapaBinding binding) {
        SupportMapFragment mapFragment
                = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null)
            mapFragment.getMapAsync(this);
    }

    // -=-=-=-=-=-=-=-=[CALLBACKS]=-=-=-=-=-=-=-=-
    // OnMapReadyCallback
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (hasLocationPermissions())
            mMap.setMyLocationEnabled(true);
        // Stub LocationSource to avoid battery drain
        // because we already have the location pending handler
        mMap.setLocationSource(STUB_LOCATION_SOURCE);
        UiSettings mapUiSettings = mMap.getUiSettings();
        mapUiSettings.setZoomControlsEnabled(true);
        mapUiSettings.setMapToolbarEnabled(true);
        mapUiSettings.setCompassEnabled(true);
        mapUiSettings.setMyLocationButtonEnabled(true);
        // Set listeners
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        // Create a shuttle/reusable adapter
        mInfoWindowView = new InfoWindowView(mViewModel.getSettings());
        mMap.setInfoWindowAdapter(mInfoWindowView);

        // Subscribe to ViewModel observables
        mViewModel.getCity().observe(this, city -> {
            mInfoWindowView.setCity(city);
            LatLng latLng = new LatLng(city.getLatitude(), city.getLongitude());
            mMarker = mMap.addMarker(new MarkerOptions().position(latLng));
            mMarker.setIcon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            // Move to marker position
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, mZoom));
        });

        mViewModel.getAverages().observe(this, averages -> {
            int orientation = requireContext().getResources().getConfiguration().orientation;
            mInfoWindowView // A little trick to change the count of data displayed on the map.
                    .setAverages(averages.subList(0, DAYS_TO_DISPLAY_ON_MAP - orientation));
            mMarker.showInfoWindow();
            // Get map width & height
            int mapWidth = mBinding.getRoot().getWidth();
            int mapHeight = mBinding.getRoot().getHeight();
            // Get marker view width & height
            int infoWidth = mInfoWindowView.getRoot().getWidth();
            int infoHeight = mInfoWindowView.getRoot().getHeight();
            Projection projection = mMap.getProjection();
            Point point = new Point(mapWidth / 2, (mapHeight - infoHeight) / 10);
            LatLng latLng = projection.fromScreenLocation(point);
            // Slowly scroll the marker down
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng), 250, null);
        });
    }

    // GoogleMap.OnMyLocationClickListener
    @Override
    public void onMyLocationClick(@NonNull Location location) {
        mViewModel.onMapaLocationClicked(location.getLatitude(), location.getLongitude());
    }

    // GoogleMap.OnMyLocationButtonClickListener
    @Override
    public boolean onMyLocationButtonClick() {
        mViewModel.onMapaLocationButtonClicked();

        return false;
    }

    // GoogleMap.OnMapClickListener
    @Override
    public void onMapClick(LatLng latLng) {
        mViewModel.onMapaClicked(latLng.latitude, latLng.longitude);
    }

    // GoogleMap.OnMapLongClickListener
    @Override
    public void onMapLongClick(LatLng latLng) {
        mViewModel.onMapaClicked(latLng.latitude, latLng.longitude);
    }

    // OnCameraMoveListener
    @Override
    public void onCameraMove() {
        CameraPosition cameraPosition = mMap.getCameraPosition();
        mZoom = cameraPosition.zoom;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        LatLng latLng = marker.getPosition();
        mViewModel.onMapaClicked(latLng.latitude, latLng.longitude);
        return true;
    }
    // ----------------[CALLBACKS]----------------

    private boolean hasLocationPermissions() {
        return ActivityCompat.checkSelfPermission(requireContext(),
                "android.permission.ACCESS_FINE_LOCATION") == 0
                &&
                ActivityCompat.checkSelfPermission(requireContext(),
                        "android.permission.ACCESS_COARSE_LOCATION") == 0;
    }

    private class InfoWindowView implements GoogleMap.InfoWindowAdapter {

        private final Settings mSettings;
        private View mRoot;
        private City mCity;
        private List<Average> mAverages;

        InfoWindowView(Settings settings) {
            mSettings = settings;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            ViewMarkerBinding binding = DataBindingUtil
                    .inflate(getLayoutInflater(), R.layout.view_marker, null, true);

            binding.miniAveragesRecycler.setLayoutManager(new LinearLayoutManager(requireContext(),
                    LinearLayoutManager.VERTICAL, false));
            binding.miniAveragesRecycler.setAdapter(new MiniAverageAdapter());

            binding.setCity(mCity);
            binding.setAverages(mAverages);
            binding.setSettings(mSettings);
            binding.executePendingBindings();

            return mRoot = binding.getRoot();
        }

        public void setCity(City city) {
            mCity = city;
        }

        public void setAverages(List<Average> averages) {
            mAverages = averages;
        }

        public View getRoot() {
            return mRoot;
        }
    }
}