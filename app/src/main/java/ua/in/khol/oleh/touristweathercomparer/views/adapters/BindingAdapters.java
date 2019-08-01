package ua.in.khol.oleh.touristweathercomparer.views.adapters;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ua.in.khol.oleh.touristweathercomparer.helpers.Dictionary;
import ua.in.khol.oleh.touristweathercomparer.helpers.LocaleUnits;

public class BindingAdapters {

    @BindingAdapter("bind:image")
    public static void loadBanner(ImageView imageView, String path) {
        Picasso.get()
                .load(path)
                .into(imageView);
    }

    @BindingAdapter("bind:date")
    public static void putDate(TextView textView, int date) {
        long time = date * 1000L;
        Date raw = new Date(time);
        SimpleDateFormat format
                = new SimpleDateFormat("EEE dd MMM yyyy", Locale.getDefault());

        textView.setText(format.format(raw));
    }

    @BindingAdapter("bind:translate")
    public static void doTranslate(TextView textView, String text) {
        textView.setText(Dictionary.translate(text,
                Locale.getDefault().toString()));
    }

    @BindingAdapter({"bind:tempLow", "bind:tempHigh"})
    public static void temLowHigh(TextView textView, float tempLow, float tempHigh) {

        boolean celsius = LocaleUnits.getInstance().isCelsius();
        String firstSign = (tempLow > 0) ? "+" : "";
        String secondSign = (tempHigh > 0) ? "+" : "";
        String thirdSign = celsius ? "\u2103" : "\u2109";
        float low = celsius ? (tempLow - 32) * 5 / 9 : tempLow;
        float high = celsius ? (tempHigh - 32) * 5 / 9 : tempHigh;

        String formatted = String.format(Locale.getDefault(), "%s%.1f..%s%.1f%s",
                firstSign, low, secondSign, high, thirdSign);
        textView.setText(formatted);
    }

    @BindingAdapter("bind:current")
    public static void tempCurrent(TextView textView, float currentTemp) {

        boolean celsius = LocaleUnits.getInstance().isCelsius();
        String firstSign = (currentTemp > 0) ? "+" : "";
        String secondSign = celsius ? "\u2103" : "\u2109";
        float current = celsius ? (currentTemp - 32) * 5 / 9 : currentTemp;

        String formatted = String.format(Locale.getDefault(), "%s%.1f%s",
                firstSign, current, secondSign);
        textView.setText(formatted);
    }
}
