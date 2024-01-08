package ua.in.khol.oleh.touristweathercomparer.ui.widgets;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ViewSwitcher;

import androidx.annotation.StringRes;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import ua.in.khol.oleh.touristweathercomparer.R;

public class ProgressButton extends ViewSwitcher {

    private static final int BUTTON_POSITION = 0;
    private static final int INDICATOR_POSITION = 1;
    private static final int[][] BUTTON_STATES = new int[][]{
            new int[]{-android.R.attr.state_enabled}, // disabled
            new int[]{android.R.attr.state_pressed}, // pressed
            new int[]{-android.R.attr.state_checked}, // unchecked
            new int[]{android.R.attr.state_enabled}, // enabled
    };

    private final MaterialButton button;
    private final CircularProgressIndicator indicator;
    private boolean isProgressing;

    public ProgressButton(Context context) {
        this(context, null);
    }

    public ProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.widget_progress_button, this);
        (button = (MaterialButton) getChildAt(BUTTON_POSITION))
                .setTextColor(new ColorStateList(BUTTON_STATES, new int[]{
                        getResources().getColor(R.color.gray),
                        getResources().getColor(R.color.colorAccentDark),
                        getResources().getColor(R.color.colorAccent),
                        getResources().getColor(R.color.colorAccent)
                }));
        indicator = (CircularProgressIndicator) getChildAt(INDICATOR_POSITION);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressButton);
        Resources resources = getResources();
        try {
            setEnabled(typedArray.getBoolean(R.styleable.ProgressButton_android_enabled, true));
            setText(typedArray.getString(R.styleable.ProgressButton_android_text));
            setBackgroundColor(typedArray.getColor(R.styleable.ProgressButton_android_background, resources.getColor(R.color.transparent)));
            setIndicatorColor(typedArray.getColor(R.styleable.ProgressButton_indicatorColor, resources.getColor(R.color.colorAccent)));
            setIndicatorSize((int) typedArray.getDimension(R.styleable.ProgressButton_indicatorSize, resources.getDimension(R.dimen.circular_progress_size)));
        } finally {
            typedArray.recycle();
        }
        setProgressing(false);
    }

    private void setIndicatorColor(int color) {
        indicator.setIndicatorColor(color);
    }

    private void setIndicatorSize(int size) {
        indicator.setIndicatorSize(size);
    }

    @Override
    public void setClickable(boolean clickable) {
        super.setClickable(clickable);
        button.setClickable(clickable);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        button.setEnabled(enabled);
    }

    private void setText(String text) {
        button.setText(text);
    }

    public void setText(@StringRes int resId) {
        button.setText(resId);
    }

    public boolean isProgressing() {
        return isProgressing;
    }

    public void setProgressing(boolean progressing) {
        if (isProgressing == progressing) return;
        isProgressing = progressing;

        setClickable(!isProgressing);
        setDisplayedChild(isProgressing ? INDICATOR_POSITION : BUTTON_POSITION);
    }

    @Override
    public void setOnClickListener(OnClickListener listener) {
        button.setOnClickListener(listener);
    }
}
