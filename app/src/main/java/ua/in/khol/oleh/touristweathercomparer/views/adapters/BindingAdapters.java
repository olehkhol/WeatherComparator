package ua.in.khol.oleh.touristweathercomparer.views.adapters;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ua.in.khol.oleh.touristweathercomparer.utils.Dictionary;
import ua.in.khol.oleh.touristweathercomparer.utils.LocaleUnits;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Provider;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Title;

public final class BindingAdapters {

    private BindingAdapters() {

    }

    @BindingAdapter("visibility")
    public static void setVisibility(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("image")
    public static void loadBanner(ImageView imageView, String path) {
        Picasso.get()
                .load(path)
                .into(imageView);
    }

    @BindingAdapter("date")
    public static void putDate(TextView textView, int date) {
        long time = date * 1000L;
        Date raw = new Date(time);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format
                = new SimpleDateFormat("EEE dd MMM yyyy");

        textView.setText(format.format(raw));
    }

    @BindingAdapter("translate")
    public static void doTranslate(TextView textView, String text) {
        textView.setText(Dictionary.translate(text,
                LocaleUnits.getInstance().getLanguage()));
    }

    @BindingAdapter({"tempLow", "tempHigh"})
    public static void temLowHigh(TextView textView, float tempLow, float tempHigh) {

        boolean celsius = LocaleUnits.getInstance().isCelsius();
        float low = celsius ? (tempLow - 32) * 5 / 9 : tempLow;
        float high = celsius ? (tempHigh - 32) * 5 / 9 : tempHigh;
        String firstSign = (low > 0) ? "+" : "";
        String secondSign = (high > 0) ? "+" : "";
        String thirdSign = celsius ? "\u2103" : "\u2109";

        @SuppressLint("DefaultLocale")
        String formatted
                = String.format("%s%.1f..%s%.1f%s", firstSign, low, secondSign, high, thirdSign);
        textView.setText(formatted);
    }

    @BindingAdapter("current")
    public static void tempCurrent(TextView textView, float currentTemp) {

        boolean celsius = LocaleUnits.getInstance().isCelsius();
        float current = celsius ? (currentTemp - 32) * 5 / 9 : currentTemp;
        String firstSign = (current > 0) ? "+" : "";
        String secondSign = celsius ? "\u2103" : "\u2109";

        @SuppressLint("DefaultLocale")
        String formatted = String.format("%s%.1f%s", firstSign, current, secondSign);
        textView.setText(formatted);
    }

    @BindingAdapter("adapter")
    public static void titlesAdapter(RecyclerView recyclerView, List<Title> titles) {
        RecyclerAdapter<Title> adapter = (RecyclerAdapter<Title>) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.clearItems();
            adapter.addItems(titles);
//            adapter.notifyDataSetChanged();
        }
    }

    @BindingAdapter("adapter")
    public static void providersAdapter(RecyclerView recyclerView, List<Provider> providers) {
        UpperAdapter adapter = (UpperAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.clearItems();
            adapter.addItems(providers);
//            adapter.notifyDataSetChanged();
        }
    }
}
