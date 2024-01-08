package ua.in.khol.oleh.touristweathercomparer.ui.widgets;

import static ua.in.khol.oleh.touristweathercomparer.utils.ContextUtils.getLocalizedContext;
import static ua.in.khol.oleh.touristweathercomparer.utils.DrawableUtils.getDrawableFromAssets;
import static ua.in.khol.oleh.touristweathercomparer.utils.DrawableUtils.resizeDrawable;
import static ua.in.khol.oleh.touristweathercomparer.utils.LocaleUtils.getCurrentLocale;
import static ua.in.khol.oleh.touristweathercomparer.utils.StringUtils.capitalizeFirst;
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
import ua.in.khol.oleh.touristweathercomparer.data.entity.Hourly;
import ua.in.khol.oleh.touristweathercomparer.utils.TimestampUtils;

public class HourlyWidget extends MaterialCardView {

    private MaterialTextView date;
    private MaterialTextView description;
    private MaterialTextView humidity;
    private MaterialTextView speed;
    private MaterialTextView temp;
    private ShapeableImageView arrow;
    private MaterialTextView direction;
    private MaterialTextView pressure;

    public HourlyWidget(@NonNull Context context) {
        super(context);
        init(context);
    }

    public HourlyWidget(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View view = inflate(context, R.layout.widget_hourly_weather, this);

        date = view.findViewById(R.id.date);
        description = view.findViewById(R.id.description);
        humidity = view.findViewById(R.id.humidity);
        speed = view.findViewById(R.id.speed);
        temp = view.findViewById(R.id.temp);
        arrow = view.findViewById(R.id.arrow);
        direction = view.findViewById(R.id.direction);
        pressure = view.findViewById(R.id.pressure);
    }

    public void setHourly(Hourly hourly) {
        Locale locale = getCurrentLocale(hourly.language);
        Context context = getLocalizedContext(getContext(), locale);
        Resources resources = context.getResources();
        String[] unitArray = resources.getStringArray(R.array.unit_values);
        String[] temperatureArray = resources.getStringArray(R.array.temperature_values);
        String[] speedArray = resources.getStringArray(R.array.speed_values);
        String[] directionArray = resources.getStringArray(R.array.directions);
        int unitsPosition = Arrays.asList(unitArray).indexOf(hourly.units);

        date.setText(TimestampUtils.toHourMin(hourly.date, locale));
        temp.setText(
                context.getString(R.string.hourly_temperature,
                        hourly.tempMin,
                        hourly.tempMax,
                        temperatureArray[unitsPosition])
        );
        setDescription(context, hourly, locale);
        humidity.setText(context.getString(R.string.humidity, hourly.humidity));
        pressure.setText(context.getString(R.string.pressure, hourly.pressure));
        speed.setText(context.getString(R.string.wind, hourly.speed, speedArray[unitsPosition]));
        arrow.setRotation(hourly.degree);
        direction.setText(context.getString(
                R.string.string,
                directionArray[getDirectionIndex(hourly.degree)])
        );
    }

    private void setDescription(Context context, Hourly hourly, Locale locale) {
        String fileName = context.getString(R.string.asset_png, hourly.image);
        int size = context.getResources().getDimensionPixelSize(R.dimen.hourly_icon_size);
        Drawable miniIconDrawable = resizeDrawable(getDrawableFromAssets(context, fileName), size, size);

        description.setText(capitalizeFirst(hourly.description, locale));
        description.setCompoundDrawables(miniIconDrawable, null, null, null);
    }
}