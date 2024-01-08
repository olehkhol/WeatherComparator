package ua.in.khol.oleh.touristweathercomparer.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.inject.Inject;

import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.data.ViewModelProviderFactory;
import ua.in.khol.oleh.touristweathercomparer.ui.widgets.ProgressButton;

public abstract class DialogFragmentView<VM extends DialogFragmentViewModel>
        extends DialogFragment {

    private static final int DEFAULT_WIDTH_IN_PERCENTAGE = 85;

    private VM viewModel;
    protected MaterialTextView tvTitle;
    protected MaterialTextView tvCaption;
    protected ProgressButton btnAccept;
    protected MaterialButton btnDismiss;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        viewModel = new ViewModelProvider(this, viewModelProviderFactory).get(getViewModelClass());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_dialog_fragment, container, false);

        ViewStub viewStub = view.findViewById(R.id.vs_holder);
        viewStub.setLayoutResource(getLayoutResId());
        viewStub.inflate();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // Force set 'no title' for android 4.4 and 5
        setNoTitle(dialog);

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        setWidthInPercentage(requireDialog(), DEFAULT_WIDTH_IN_PERCENTAGE);
    }

    protected VM getViewModel() {
        return viewModel;
    }

    protected abstract int getLayoutResId();

    protected void initView(View view) {
        tvTitle = view.findViewById(R.id.mtv_title);
        tvCaption = view.findViewById(R.id.mtv_caption);
        btnAccept = view.findViewById(R.id.btn_accept);
        (btnDismiss = view.findViewById(R.id.btn_dismiss)).setOnClickListener(v -> dismiss());
    }

    private void setNoTitle(Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null)
            window.requestFeature(Window.FEATURE_NO_TITLE);
    }

    protected void setFullWidth(Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null)
            window.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
    }

    protected void setWidthInPercentage(Dialog dialog, int percentage) {
        Window window = dialog.getWindow();
        if (window != null) {
            float percent = (float) percentage / 100;
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            Rect rect = new Rect(0, 0, metrics.widthPixels, metrics.heightPixels);
            float percentWidth = rect.width() * percent;
            window.setLayout(
                    (int) percentWidth,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }

    protected void setAccepting(boolean isAccepting) {
        btnAccept.setProgressing(isAccepting);
    }

    /**
     * Magic for FragmentViewModel
     *
     * @return generic magic
     */
    private Class<VM> getViewModelClass() {
        Class<? extends DialogFragmentView> clazz = getClass();
        Type genericSuperclass = clazz.getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
        return (Class<VM>) actualTypeArgument;
    }
}