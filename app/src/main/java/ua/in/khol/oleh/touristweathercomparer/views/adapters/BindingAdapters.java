package ua.in.khol.oleh.touristweathercomparer.views.adapters;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.model.settings.Settings;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Average;

public final class BindingAdapters {

    private BindingAdapters() {
    }

    @BindingAdapter("visibility")
    public static void visibility(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    @BindingAdapter("image")
    public static void image(ImageView imageView, String path) {
        Picasso.get()
                .load(path)
                .into(imageView);
    }

    @BindingAdapter("date")
    public static void date(TextView textView, int date) {
        long time = date * 1000L;
        Date raw = new Date(time);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format
                = new SimpleDateFormat("EEE dd MMM");

        textView.setText(format.format(raw));
    }

    @BindingAdapter("time")
    public static void time(TextView textView, int date) {
        long time = date * 1000L;
        Date raw = new Date(time);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format
                = new SimpleDateFormat("HH:mm:ss");

        textView.setText(format.format(raw));
    }

    @BindingAdapter({"tempLow", "tempHigh", "index"})
    public static void lowHigh(TextView textView, float tempLow, float tempHigh, int index) {
        Resources resources = textView.getContext().getResources();
        float low;
        float high;

        switch (index) {
            case 0:
                low = (tempLow - 32) * 5 / 9;
                high = (tempHigh - 32) * 5 / 9;
                break;
            case 1:
            default:
                low = tempLow;
                high = tempHigh;
                break;
            case 2:
                low = (tempLow + 459.67f) * 5 / 9;
                high = (tempHigh + 459.67f) * 5 / 9;
                break;
        }
        String firstSign = (low > 0) ? "+" : "";
        String secondSign = (high > 0) ? "+" : "";

        @SuppressLint("DefaultLocale")
        String formatted = String.format("%s%.1f..%s%.1f%s", firstSign, low, secondSign, high,
                resources.getStringArray(R.array.temperature_values)[index]);
        textView.setText(formatted);
    }

    @BindingAdapter({"current", "index"})
    public static void current(TextView textView, float current, int index) {
        Resources resources = textView.getContext().getResources();
        float temperature;

        switch (index) {
            case 0:
                temperature = (current - 32) * 5 / 9;
                break;
            case 1:
            default:
                temperature = current;
                break;
            case 2:
                temperature = (current + 459.67f) * 5 / 9;
                break;
        }
        String firstSign = (temperature > 0) ? "+" : "";

        @SuppressLint("DefaultLocale")
        String formatted = String.format("%s%.1f%s", firstSign,
                temperature, resources.getStringArray(R.array.temperature_values)[index]);
        textView.setText(formatted);

    }

    @BindingAdapter({"speed", "index"})
    public static void speed(TextView textView, float mph, int index) {
        Resources resources = textView.getContext().getResources();
        float speed;

        switch (index) {
            case 0:
                speed = mph * 0.44704f;
                break;
            case 2:
            default:
                speed = mph;
                break;
            case 1:
                speed = mph * 1.609344f;
                break;
        }
        String formatted = String.format(resources.getString(R.string.wind),
                speed, resources.getStringArray(R.array.speed_values)[index]);
        textView.setText(formatted);
    }

    @BindingAdapter({"pressure", "index"})
    public static void pressure(TextView textView, float hpa, int index) {
        Resources resources = textView.getContext().getResources();
        float pressure;

        switch (index) {
            case 0:
                pressure = hpa * 0.7500616827f;
                break;
            case 1:
            case 2:
            default:
                pressure = hpa;
                break;
        }
        String formatted = String.format(resources.getString(R.string.pressure),
                pressure, resources.getStringArray(R.array.pressure_values)[index]);
        textView.setText(formatted);
    }

    @BindingAdapter({"averages", "settings"})
    public static void averages(RecyclerView recyclerView,
                                List<Average> averages, Settings settings) {
        if (averages != null && averages.size() > 0) {
            AverageAdapter adapter = (AverageAdapter) recyclerView.getAdapter();
            adapter.setSettings(settings);
            adapter.setItems(averages);
        }
    }

    @BindingAdapter("canapes")
    public static void canapes(RecyclerView recyclerView, List<Average.Canape> canapes) {
        if (canapes != null && canapes.size() > 0) {
            CanapeAdapter adapter = (CanapeAdapter) recyclerView.getAdapter();
            adapter.setItems(canapes);
        }
    }

    @BindingAdapter({"miniAverages", "settings"})
    public static void miniAverages(RecyclerView recyclerView,
                                    List<Average> averages, Settings settings) {
        if (averages != null && averages.size() > 0) {
            MiniAverageAdapter adapter = (MiniAverageAdapter) recyclerView.getAdapter();
            adapter.setSettings(settings);
            adapter.setItems(averages);
        }
    }

    @BindingAdapter("minipes")
    public static void minipes(RecyclerView recyclerView, List<Average.Canape> minipes) {
        if (minipes != null && minipes.size() > 0) {
            MinipeAdapter adapter = (MinipeAdapter) recyclerView.getAdapter();
            adapter.setItems(minipes);
        }
    }
}
