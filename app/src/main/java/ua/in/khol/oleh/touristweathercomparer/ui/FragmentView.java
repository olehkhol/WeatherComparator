package ua.in.khol.oleh.touristweathercomparer.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Locale;

import javax.inject.Inject;

import ua.in.khol.oleh.touristweathercomparer.data.ViewModelProviderFactory;

public abstract class FragmentView<VM extends FragmentViewModel>
        extends Fragment {

    private VM viewModel;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Override
    public void onAttach(@NonNull Context context) {
        viewModel = new ViewModelProvider(this, viewModelProviderFactory)
                .get(getViewModelClass());
        updateLocale(viewModel.getLanguage());

        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResId(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    protected abstract int getLayoutResId();

    protected abstract void initView(View view);

    protected VM getViewModel() {
        return viewModel;
    }

    /**
     * Determines the ViewModel class type for a Fragment.
     * Similar to its activity counterpart, this method uses Java reflection
     * to find the ViewModel type. It inspects the generic superclass of the
     * FragmentView to identify and return the ViewModel's class type.
     *
     * @return The ViewModel class type specifically for the Fragment.
     */
    private Class<VM> getViewModelClass() {
        Class<? extends FragmentView> aClass = getClass();
        Type genericSuperclass = aClass.getGenericSuperclass();
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
}