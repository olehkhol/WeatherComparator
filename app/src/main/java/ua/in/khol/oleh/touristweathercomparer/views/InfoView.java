package ua.in.khol.oleh.touristweathercomparer.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.databinding.ViewInfoBinding;
import ua.in.khol.oleh.touristweathercomparer.di.ViewModelProviderFactory;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.InfoViewModel;

public class InfoView extends Fragment implements ViewBinding<ViewInfoBinding> {

    @Inject
    ViewModelProviderFactory mViewModelProviderFactory;

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewInfoBinding binding = ViewInfoBinding.inflate(inflater, container, false);
        InfoViewModel infoViewModel = new ViewModelProvider(this, mViewModelProviderFactory)
                .get(InfoViewModel.class);
        binding.setInfoViewModel(infoViewModel);
        initBinding(binding);

        return binding.getRoot();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initBinding(ViewInfoBinding binding) {
        WebView webView = binding.webView;
        webView.getSettings().setJavaScriptEnabled(true);
        String fileName = getResources().getString(R.string.info_file);
        webView.loadUrl("file:///android_asset/" + fileName);
    }

}
