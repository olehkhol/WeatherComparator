package ua.in.khol.oleh.touristweathercomparer.ui;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Locale;

import javax.inject.Inject;

import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.data.ViewModelProviderFactory;

public abstract class ActivityView<VM extends ActivityViewModel>
        extends AppCompatActivity
        implements ViewModelStoreOwner {

    private VM viewModel;
    protected Toolbar toolbar;
    private Observer<String> languageObserver;
    private Observer<Integer> themeCodeObserver;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, viewModelProviderFactory)
                .get(getViewModelClass());
        updateLocale(viewModel.getLanguageCode());
        updateTheme(viewModel.getThemeCode());

        super.onCreate(savedInstanceState);

        setContentView(getLayoutResId());
        initContentView();
    }

    protected abstract int getLayoutResId();

    protected abstract int getTitleResId();

    protected void initContentView() {
        (toolbar = findViewById(R.id.toolbar)).setTitle(getTitleResId());
        setSupportActionBar(toolbar);
    }

    protected void refresh() {
        toolbar.setTitle(getTitleResId());
    }

    @Override
    protected void onResume() {
        super.onResume();

        viewModel.getLanguageEvent().observe(this, languageObserver = language -> {
            updateLocale(language);
            refresh();
        });
        viewModel.getThemeCodeEvent().observe(this, themeCodeObserver = themeCode -> {
            updateTheme(themeCode);
            refresh();
        });
    }

    @Override
    protected void onPause() {
        viewModel.getThemeCodeEvent().removeObserver(themeCodeObserver);
        viewModel.getLanguageEvent().removeObserver(languageObserver);

        super.onPause();
    }

    /**
     * Retrieves the class type of the ViewModel associated with an Activity.
     * This method uses reflection to determine the actual type of ViewModel
     * required by the Activity. It navigates up the class hierarchy to find
     * the generic superclass and then extracts the type parameter to get the
     * concrete class of the ViewModel.
     *
     * @return The class type of the ViewModel specific to the Activity.
     */
    private Class<VM> getViewModelClass() {
        Class<? extends ActivityView> aClass = getClass();
        Class superclass = aClass.getSuperclass();
        Type genericSuperclass = superclass.getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];

        return (Class<VM>) actualTypeArgument;
    }

    protected void updateLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = getResources().getConfiguration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private void updateTheme(int themeCode) {
        getDelegate().setLocalNightMode(themeCode);
    }
}