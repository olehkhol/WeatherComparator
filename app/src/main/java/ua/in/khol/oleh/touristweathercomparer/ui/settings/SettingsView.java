package ua.in.khol.oleh.touristweathercomparer.ui.settings;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.NavUtils;

import ua.in.khol.oleh.touristweathercomparer.MainApplication;
import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.ui.ActivityView;

public class SettingsView extends ActivityView<SettingsViewModel> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ((MainApplication) getApplication())
                .getApplicationComponent().inject(this);
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        refresh();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.view_settings;
    }

    @Override
    protected int getTitleResId() {
        return R.string.settings;
    }

    @Override
    protected void refresh() {
        super.refresh();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.preference_container, new PreferenceView())
                .commit();
    }
}