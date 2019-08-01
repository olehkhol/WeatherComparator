package ua.in.khol.oleh.touristweathercomparer.helpers;

import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Oleh Kholiavchuk on 22-Jan-18.
 */

public class MarketView {

    /**
     * Opens application page in Google Play
     **/
    public static void open(AppCompatActivity activity, String appPackageName) {
        try {
            activity.startActivity(
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            activity.startActivity(
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id="
                                    + appPackageName)));
        }
    }
}
