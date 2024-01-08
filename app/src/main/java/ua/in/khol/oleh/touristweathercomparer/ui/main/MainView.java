package ua.in.khol.oleh.touristweathercomparer.ui.main;

import static ua.in.khol.oleh.touristweathercomparer.Globals.MENU_ITEM_SEARCH;
import static ua.in.khol.oleh.touristweathercomparer.Globals.MENU_ITEM_SETTINGS;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.customview.widget.Openable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

import java.lang.ref.WeakReference;

import ua.in.khol.oleh.touristweathercomparer.MainApplication;
import ua.in.khol.oleh.touristweathercomparer.MainSettings;
import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.ui.ActivityView;
import ua.in.khol.oleh.touristweathercomparer.ui.search.SearchView;

public class MainView extends ActivityView<MainViewModel> {

    private static final int[][] NAVIGATION_STATES = new int[][]{
            new int[]{-android.R.attr.state_enabled}, // disabled
            new int[]{android.R.attr.state_pressed},  // pressed
            new int[]{-android.R.attr.state_checked}, // unchecked
            new int[]{android.R.attr.state_enabled}, // enabled
    };

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private NavHostFragment navHostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((MainApplication) getApplication())
                .getApplicationComponent().inject(this);
        super.onCreate(savedInstanceState);

        ColorStateList navigationTextColorStateList = new ColorStateList(NAVIGATION_STATES, new int[]{
                getResources().getColor(R.color.colorPrimaryDark),
                getResources().getColor(R.color.colorAccentDark),
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorAccent)
        });
        ColorStateList navigationIconColorStateList = new ColorStateList(NAVIGATION_STATES, new int[]{
                getResources().getColor(R.color.colorPrimaryDark),
                getResources().getColor(R.color.colorAccentDark),
                getResources().getColor(R.color.colorPrimaryDark),
                getResources().getColor(R.color.colorAccentDark)
        });
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemTextColor(navigationTextColorStateList);
        navigationView.setItemIconTintList(navigationIconColorStateList);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setItemTextColor(navigationTextColorStateList);
        bottomNavigationView.setItemIconTintList(navigationIconColorStateList);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_view);

        if (navHostFragment == null) {
            navHostFragment = NavHostFragment.create(R.navigation.mobile_navigation);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, navHostFragment)
                    .setPrimaryNavigationFragment(navHostFragment)
                    .commit();

        }
        navHostFragment.getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
            if (event.getTargetState().equals(Lifecycle.State.CREATED)) {
                NavController navController = navHostFragment.getNavController();
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainView.this, drawerLayout, toolbar, 0, 0);
                drawerLayout.addDrawerListener(toggle);
                toggle.syncState();

                setupWithNavController(bottomNavigationView, navController);
                setupWithNavController(navigationView, navController);
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.view_main;
    }

    @Override
    protected int getTitleResId() {
        return R.string.app_name;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem miOne = menu.add(0, MENU_ITEM_SEARCH, 0, null);
        miOne.setIcon(R.drawable.abc_btn_check_material_background_anim);
        miOne.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        MenuItem miTwo = menu.add(0, MENU_ITEM_SETTINGS, 0, null);
        miTwo.setIcon(R.drawable.abc_btn_default_mtrl_background);
        miTwo.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        miTwo.setVisible(true);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM_SEARCH -> {
                showSearchView();
                return true;
            }
            case MENU_ITEM_SETTINGS -> {
                showSettingsView();
                return true;
            }
            default -> {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    // Called only when set up using NavigationUI.setupActionBarWithNavController.
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = navHostFragment.getNavController();

        return NavigationUI.navigateUp(navController, drawerLayout)
                || super.onSupportNavigateUp();
    }

    /**
     * Sets up a {@link BottomNavigationView} for use with a {@link NavController}. This will call
     * {@link NavigationUI .onNavDestinationSelected(MenuItem, NavController)} when a menu item is selected. The
     * selected item in the BottomNavigationView will automatically be updated when the destination
     * changes.
     *
     * @param bottomNavigationView The BottomNavigationView that should be kept in sync with
     *                             changes to the NavController.
     * @param navController        The NavController that supplies the primary menu.
     *                             Navigation actions on this NavController will be reflected in the
     *                             selected item in the BottomNavigationView.
     */
    private void setupWithNavController(BottomNavigationView bottomNavigationView, final NavController navController) {
        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> NavigationUI.onNavDestinationSelected(item, navController));

        final WeakReference<BottomNavigationView> bottomNavigationViewWeakReference =
                new WeakReference<>(bottomNavigationView);

        navController.addOnDestinationChangedListener(
                new NavController.OnDestinationChangedListener() {
                    @Override
                    public void onDestinationChanged(@NonNull NavController controller,
                                                     @NonNull NavDestination destination, @Nullable Bundle arguments) {
                        BottomNavigationView view = bottomNavigationViewWeakReference.get();
                        if (view == null) {
                            navController.removeOnDestinationChangedListener(this);
                            return;
                        }
                        Menu bottomNavigationMenu = view.getMenu();
                        Menu drawerNavigationMenu = navigationView.getMenu().getItem(0).getSubMenu();
                        for (int h = 0, size = bottomNavigationMenu.size(); h < size; h++) {
                            MenuItem item = bottomNavigationMenu.getItem(h);
                            if (matchDestination(destination, item.getItemId())) {
                                item.setChecked(true);
                                // Also highlight the navigation drawer menu item
                                drawerNavigationMenu.getItem(h).setChecked(true);
                            } else
                                // Or dim the navigation drawer menu item
                                drawerNavigationMenu.getItem(h).setChecked(false);
                        }
                    }

                    private boolean matchDestination(@NonNull NavDestination destination,
                                                     @IdRes int destId) {
                        NavDestination currentDestination = destination;
                        while (currentDestination.getId() != destId && currentDestination.getParent() != null) {
                            currentDestination = currentDestination.getParent();
                        }
                        return currentDestination.getId() == destId;
                    }
                });
    }

    /**
     * Sets up a {@link NavigationView} for use with a {@link NavController}. This will call
     * {@link NavigationUI .onNavDestinationSelected(MenuItem, NavController)} when a menu item is selected.
     * The selected item in the NavigationView will automatically be updated when the destination
     * changes.
     * <p>
     * If the {@link NavigationView} is directly contained with an {@link Openable} layout,
     * it will be closed when a menu item is selected.
     * <p>
     * Similarly, if the {@link NavigationView} has a {@link BottomSheetBehavior} associated with
     * it (as is the case when using a {@link com.google.android.material.bottomsheet.BottomSheetDialog}),
     * the bottom sheet will be hidden when a menu item is selected.
     *
     * @param navigationView The NavigationView that should be kept in sync with changes to the
     *                       NavController.
     * @param navController  The NavController that supplies the primary and secondary menu.
     *                       Navigation actions on this NavController will be reflected in the
     *                       selected item in the NavigationView.
     */
    private void setupWithNavController(final NavigationView navigationView, final NavController navController) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
                        if (handled) {
                            ViewParent parent = navigationView.getParent();
                            if (parent instanceof Openable)
                                ((Openable) parent).close();
                            else {
                                BottomSheetBehavior bottomSheetBehavior = findBottomSheetBehavior(navigationView);
                                if (bottomSheetBehavior != null)
                                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            }
                        }

                        return handled;
                    }

                    private BottomSheetBehavior findBottomSheetBehavior(@NonNull View view) {
                        ViewGroup.LayoutParams params = view.getLayoutParams();
                        if (!(params instanceof CoordinatorLayout.LayoutParams)) {
                            ViewParent parent = view.getParent();
                            if (parent instanceof View)
                                return findBottomSheetBehavior((View) parent);

                            return null;
                        }
                        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) params).getBehavior();
                        if (behavior instanceof BottomSheetBehavior)
                            return (BottomSheetBehavior) behavior;

                        // We hit a CoordinatorLayout, but the View doesn't have the BottomSheetBehavior
                        return null;
                    }
                });

        final WeakReference<NavigationView> navigationViewWeakReference = new WeakReference<>(navigationView);

        navController.addOnDestinationChangedListener(
                new NavController.OnDestinationChangedListener() {
                    @Override
                    public void onDestinationChanged(@NonNull NavController controller,
                                                     @NonNull NavDestination destination, @Nullable Bundle arguments) {
                        NavigationView view = navigationViewWeakReference.get();
                        if (view == null) {
                            navController.removeOnDestinationChangedListener(this);
                            return;
                        }
                        Menu menu = view.getMenu();
                        for (int h = 0, size = menu.size(); h < size; h++) {
                            MenuItem item = menu.getItem(h);
                            item.setChecked(matchDestination(destination, item.getItemId()));
                        }
                    }

                    private boolean matchDestination(@NonNull NavDestination destination,
                                                     @IdRes int destId) {
                        NavDestination currentDestination = destination;
                        while (currentDestination.getId() != destId
                                && currentDestination.getParent() != null)
                            currentDestination = currentDestination.getParent();

                        return currentDestination.getId() == destId;
                    }
                });
    }

    private void showSettingsView() {
        Intent intent = new Intent(this, MainSettings.class);
        startActivity(intent);
    }

    private void showSearchView() {
        SearchView.newInstance()
                .show(getSupportFragmentManager(), SearchView.TAG);
    }
}