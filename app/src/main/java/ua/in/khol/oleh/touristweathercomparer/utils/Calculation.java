package ua.in.khol.oleh.touristweathercomparer.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Calculation {

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static int roundDateToDays(int date) {
        int divided = date / 60 / 60 / 24;

        return divided * 24 * 60 * 60;
    }
}
