package ua.in.khol.oleh.touristweathercomparer.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreferenceCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ua.in.khol.oleh.touristweathercomparer.MainApplication;
import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.contracts.SettingsContract;
import ua.in.khol.oleh.touristweathercomparer.model.Repository;
import ua.in.khol.oleh.touristweathercomparer.presenters.PresenterFactory;
import ua.in.khol.oleh.touristweathercomparer.presenters.PresenterLoader;
import ua.in.khol.oleh.touristweathercomparer.presenters.SettingsPresenter;

import static ua.in.khol.oleh.touristweathercomparer.model.Preferences.CELSIUS;
import static ua.in.khol.oleh.touristweathercomparer.model.Preferences.GPS_CHECK;
import static ua.in.khol.oleh.touristweathercomparer.model.Preferences.LANGUAGE;
import static ua.in.khol.oleh.touristweathercomparer.model.Preferences.POWER;
import static ua.in.khol.oleh.touristweathercomparer.model.Preferences.TIME;

public class SettingsView extends PreferenceFragmentCompat
        implements
        View.OnKeyListener,
        SettingsContract.View,
        LoaderManager.LoaderCallbacks<SettingsContract.Presenter>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = SettingsView.class.getSimpleName();
    private Context mContext;
    private View mView;
    private boolean mCriticalChanges = false;
    // MVP variables
    private static final int LOADER_ID = 33;
    private SettingsContract.Presenter mSettingsPresenter;
    @Inject
    Repository mRepository;

    // -=-=-=-=-=-=-=-=[LIFECYCLE CALLBACKS]=-=-=-=-=-=-=-=-
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        // Dagger injection
        ((MainApplication) context.getApplicationContext())
                .getAppComponent()
                .inject(this);
        mRepository.updatePreferences();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SwitchPreferenceCompat celsius
                = (SwitchPreferenceCompat) findPreference(CELSIUS);
        celsius.setOnPreferenceChangeListener((preference, newValue) -> {
            mCriticalChanges = true;
            return true;
        });

        SwitchPreferenceCompat useGps
                = (SwitchPreferenceCompat) findPreference(GPS_CHECK);
        useGps.setOnPreferenceChangeListener((preference, newValue) -> {
            mCriticalChanges = true;
            return true;
        });

        ListPreference powerPref
                = (ListPreference) findPreference(POWER);
        powerPref.setOnPreferenceChangeListener((preference, newValue) -> {
            mCriticalChanges = true;
            return true;
        });

        ListPreference timePref
                = (ListPreference) findPreference(TIME);
        timePref.setOnPreferenceChangeListener((preference, newValue) -> {
            mCriticalChanges = true;
            return true;
        });
        // TODO unhide this preference
        timePref.setVisible(false);

        ListPreference langPref
                = (ListPreference) findPreference(LANGUAGE);
        langPref.setOnPreferenceChangeListener((preference, newValue) -> {
            mCriticalChanges = true;
            return true;
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mView = super.onCreateView(inflater, container, savedInstanceState);

        if (mView != null) {
            Context context = getContext();
            if (context != null) {
                mView.setBackgroundColor(ContextCompat
                        .getColor(context, R.color.toolbar_background));
            }
        }

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LoaderManager loaderManager = LoaderManager.getInstance(this);
        Loader<SettingsContract.Presenter> loader = loaderManager.getLoader(LOADER_ID);
        if (loader == null) {
            loaderManager.initLoader(LOADER_ID, null, this);
            loader = loaderManager.getLoader(LOADER_ID);
            if (loader != null && !loader.isReset()) {
                loaderManager.restartLoader(LOADER_ID, null, this);
            }
        } else {
            mSettingsPresenter
                    = ((PresenterLoader<SettingsContract.Presenter>) loader).getPresenter();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mSettingsPresenter != null)
            mSettingsPresenter.attachView(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        for (Preference preference : getPreferenceList(getPreferenceScreen(), new ArrayList<>())) {
            if (preference instanceof ListPreference) {
                updateListPreference((ListPreference) preference);
            }
        }
    }

    @Override
    public void onStop() {
        if (mSettingsPresenter != null) {
            if (mCriticalChanges)
                mSettingsPresenter.onValuesChanged();
            mSettingsPresenter.detachView();
        }
        super.onStop();
    }
    // ----------------[LIFECYCLE CALLBACKS]----------------

    // -=-=-=-=-=-=-=-=[VIEW CALLBACKS]=-=-=-=-=-=-=-=-
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {

                mSettingsPresenter.destroy();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(this).commit();
                fragmentManager.popBackStack();

                return true;
            }
        }

        return false;
    }
    // ----------------[VIEW CALLBACKS]----------------

    // -=-=-=-=-=-=-=-=[CONTRACT METHODS]=-=-=-=-=-=-=-=-
    @Override
    public void setPresenter(SettingsContract.Presenter presenter) {
        mSettingsPresenter = presenter;
        if (mSettingsPresenter != null) {
            mSettingsPresenter.attachView(this);
        }
    }

    @Override
    public void showError(String error) {
        Snackbar.make(mView, error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showMessage(String message) {
        Log.d(TAG, message);
    }
    // ----------------[CONTRACT METHODS]----------------

    // -=-=-=-=-=-=-=-=[LOADERMANAGER CALLBACKS]=-=-=-=-=-=-=-=-
    @NonNull
    @Override
    public Loader<SettingsContract.Presenter> onCreateLoader(int id,
                                                             @Nullable Bundle args) {
        return new PresenterLoader<>(mContext, getPresenterFactory());
    }

    private PresenterFactory<SettingsContract.Presenter> getPresenterFactory() {
        return () -> new SettingsPresenter(mRepository);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<SettingsContract.Presenter> loader,
                               SettingsContract.Presenter presenter) {
        setPresenter(presenter);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<SettingsContract.Presenter> loader) {
    }
    // ----------------[LOADERMANAGER CALLBACKS]----------------

    // -=-=-=-=-=-=-=-=[PREFERENCE CALLBACKS]=-=-=-=-=-=-=-=-
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        Preference preference = findPreference(key);
        if (preference instanceof ListPreference) {
            updateListPreference((ListPreference) preference);
        }
    }
    // ----------------[PREFERENCE CALLBACKS]----------------

    private List<Preference> getPreferenceList(Preference preference,
                                               List<Preference> list) {
        if (preference instanceof PreferenceCategory
                || preference instanceof PreferenceScreen) {
            PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
            for (int i = 0; i < preferenceGroup.getPreferenceCount(); i++) {
                getPreferenceList(preferenceGroup.getPreference(i), list);
            }
        } else {
            list.add(preference);
        }

        return list;
    }

    private void updateListPreference(ListPreference listPreference) {
        listPreference.setSummary(listPreference.getEntry());
    }
}
