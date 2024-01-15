package ua.in.khol.oleh.touristweathercomparer.ui.widgets;

import static ua.in.khol.oleh.touristweathercomparer.utils.ContextUtils.getLocalizedContext;
import static ua.in.khol.oleh.touristweathercomparer.utils.LocaleUtils.getCurrentLocale;
import static ua.in.khol.oleh.touristweathercomparer.utils.StringUtils.capitalizeFirst;
import static ua.in.khol.oleh.touristweathercomparer.utils.TimestampUtils.toWeekYearMonthDay;
import static ua.in.khol.oleh.touristweathercomparer.utils.UnitsUtils.getDirectionIndex;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.Arrays;
import java.util.Locale;

import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Daily;

public class DailyWidget extends MaterialCardView {

    private MaterialTextView date;
    private MaterialTextView time;
    private MaterialTextView humidity;
    private MaterialTextView speed;
    private MaterialTextView temp;
    private ShapeableImageView arrow;
    private MaterialTextView direction;
    private MaterialTextView pressure;

    public DailyWidget(@NonNull Context context) {
        super(context);
        init(context);
    }

    public DailyWidget(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View view = inflate(context, R.layout.widget_daily_weather, this);

        date = view.findViewById(R.id.date);
        time = view.findViewById(R.id.time);
        humidity = view.findViewById(R.id.humidity);
        speed = view.findViewById(R.id.speed);
        temp = view.findViewById(R.id.temp);
        arrow = view.findViewById(R.id.arrow);
        direction = view.findViewById(R.id.direction);
        pressure = view.findViewById(R.id.pressure);
    }

    public void setDaily(Daily daily) {
        Locale locale = getCurrentLocale(daily.language);
        Context context = getLocalizedContext(getContext(), locale);
        Resources resources = context.getResources();
        String[] unitArray = resources.getStringArray(R.array.unit_values);
        String[] temperatureArray = resources.getStringArray(R.array.temperature_values);
        String[] speedArray = resources.getStringArray(R.array.speed_values);
        String[] directionArray = resources.getStringArray(R.array.directions);
        int unitsPosition = Arrays.asList(unitArray).indexOf(daily.units);

        date.setText(capitalizeFirst(toWeekYearMonthDay(daily.date, locale), locale));
        time.setText(""); // TODO Replace with something valuable
        temp.setText(
                context.getString(R.string.hourly_temperature,
                        daily.tempMin,
                        daily.tempMax,
                        temperatureArray[unitsPosition])
        );
        humidity.setText(context.getString(R.string.humidity, daily.humidity));
        pressure.setText(context.getString(R.string.pressure, daily.pressure));
        speed.setText(context.getString(R.string.wind, daily.speed, speedArray[unitsPosition]));
        arrow.setRotation(daily.degree);
        direction.setText(context.getString(
                R.string.string,
                directionArray[getDirectionIndex(daily.degree)])
        );
    }
}