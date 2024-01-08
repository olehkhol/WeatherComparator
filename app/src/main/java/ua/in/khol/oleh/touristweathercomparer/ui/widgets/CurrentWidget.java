package ua.in.khol.oleh.touristweathercomparer.ui.widgets;

import static ua.in.khol.oleh.touristweathercomparer.utils.ContextUtils.getLocalizedContext;
import static ua.in.khol.oleh.touristweathercomparer.utils.DrawableUtils.getDrawableFromAssets;
import static ua.in.khol.oleh.touristweathercomparer.utils.DrawableUtils.resizeDrawable;
import static ua.in.khol.oleh.touristweathercomparer.utils.LocaleUtils.getCurrentLocale;
import static ua.in.khol.oleh.touristweathercomparer.utils.StringUtils.capitalizeFirst;
import static ua.in.khol.oleh.touristweathercomparer.utils.TimestampUtils.toHourMinSec;
import static ua.in.khol.oleh.touristweathercomparer.utils.UnitsUtils.getDirectionIndex;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.Arrays;
import java.util.Locale;

import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Current;

public class CurrentWidget extends MaterialCardView {

    private MaterialTextView date;
    private MaterialTextView description;
    private MaterialTextView humidity;
    private MaterialTextView speed;
    private MaterialTextView temp;
    private ShapeableImageView arrow;
    private MaterialTextView direction;
    private MaterialTextView pressure;

    public CurrentWidget(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CurrentWidget(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View view = inflate(context, R.layout.widget_current, this);

        date = view.findViewById(R.id.date);
        description = view.findViewById(R.id.description);
        humidity = view.findViewById(R.id.humidity);
        speed = view.findViewById(R.id.speed);
        temp = view.findViewById(R.id.temp);
        arrow = view.findViewById(R.id.arrow);
        direction = view.findViewById(R.id.direction);
        pressure = view.findViewById(R.id.pressure);
    }

    public void setCurrent(Current current) {
        Locale locale = getCurrentLocale(current.language);
        Context context = getLocalizedContext(getContext(), locale);
        Resources resources = context.getResources();
        String[] unitArray = resources.getStringArray(R.array.unit_values);
        String[] temperatureArray = resources.getStringArray(R.array.temperature_values);
        String[] speedArray = resources.getStringArray(R.array.speed_values);
        String[] directionArray = resources.getStringArray(R.array.directions);
        int unitsPosition = Arrays.asList(unitArray).indexOf(current.units);

        date.setText(toHourMinSec(current.date, locale));
        setDescription(context, current, locale);
        temp.setText(context.getString(R.string.current_temperature, current.temp, temperatureArray[unitsPosition]));
        humidity.setText(context.getString(R.string.humidity, current.humidity));
        pressure.setText(context.getString(R.string.pressure, current.pressure));
        speed.setText(context.getString(R.string.wind, current.speed, speedArray[unitsPosition]));
        arrow.setRotation(current.degree);
        direction.setText(context.getString(R.string.string, directionArray[getDirectionIndex(current.degree)]));
    }

    private void setDescription(Context context, Current current, Locale locale) {
        String fileName = context.getString(R.string.asset_png, current.image);
        Drawable miniIconDrawable = resizeDrawable(
                getDrawableFromAssets(context, fileName),
                context.getResources().getDimensionPixelSize(R.dimen.icon_width),
                context.getResources().getDimensionPixelSize(R.dimen.icon_height)
        );
        description.setText(capitalizeFirst(current.description, locale));
        description.setCompoundDrawables(miniIconDrawable, null, null, null);
    }
}