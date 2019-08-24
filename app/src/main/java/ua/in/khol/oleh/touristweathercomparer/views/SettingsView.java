package ua.in.khol.oleh.touristweathercomparer.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreferenceCompat;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.SettingsViewModel;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.ViewModelProviderFactory;

public class SettingsView extends PreferenceFragmentCompat
        implements View.OnKeyListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private boolean mCriticalChanges = false;
    private SettingsViewModel mViewModel;

    @Inject
    ViewModelProviderFactory mViewModelProviderFactory;

    // -=-=-=-=-=-=-=-=[LIFECYCLE CALLBACKS]=-=-=-=-=-=-=-=-
    @Override
    public void onAttach(Context context) {
        // Dagger injection
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        SwitchPreferenceCompat celsius
                = (SwitchPreferenceCompat) findPreference(getString(R.string.celsius));
        celsius.setOnPreferenceChangeListener((preference, newValue) -> {
            mCriticalChanges = true;
            return true;
        });

        SwitchPreferenceCompat useGps
                = (SwitchPreferenceCompat) findPreference(getString(R.string.gps_check));
        useGps.setOnPreferenceChangeListener((preference, newValue) -> {
            mCriticalChanges = true;
            return true;
        });

        ListPreference powerPref
                = (ListPreference) findPreference(getString(R.string.power));
        powerPref.setOnPreferenceChangeListener((preference, newValue) -> {
            mCriticalChanges = true;
            return true;
        });

        ListPreference timePref
                = (ListPreference) findPreference(getString(R.string.time));
        timePref.setOnPreferenceChangeListener((preference, newValue) -> {
            mCriticalChanges = true;
            return true;
        });
        // TODO unhide this preference
        timePref.setVisible(false);

        ListPreference langPref
                = (ListPreference) findPreference(getString(R.string.language));
        langPref.setOnPreferenceChangeListener((preference, newValue) -> {
            mCriticalChanges = true;
            return true;
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (view != null) {
            Context context = getContext();
            if (context != null) {
                view.setBackgroundColor(ContextCompat
                        .getColor(context, R.color.toolbar_background));
            }
        }

        return view;
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
        mViewModel = ViewModelProviders.of(this, mViewModelProviderFactory)
                .get(SettingsViewModel.class);
        mViewModel.wakeUp();

        super.onActivityCreated(savedInstanceState);

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
        if (mViewModel != null) {
            if (mCriticalChanges)
                mViewModel.onValuesChanged();
        }
        super.onStop();
    }
    // ----------------[LIFECYCLE CALLBACKS]----------------

    // -=-=-=-=-=-=-=-=[VIEW CALLBACKS]=-=-=-=-=-=-=-=-
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(this).commit();
                fragmentManager.popBackStack();

                return true;
            }
        }

        return false;
    }
    // ----------------[VIEW CALLBACKS]----------------

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

    // -=-=-=-=-=-=-=-=[REGULAR METHODS]=-=-=-=-=-=-=-=-
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
    // ----------------[REGULAR METHODS]----------------
}
