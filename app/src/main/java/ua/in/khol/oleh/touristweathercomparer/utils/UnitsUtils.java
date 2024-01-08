package ua.in.khol.oleh.touristweathercomparer.utils;

public class UnitsUtils {

    public static int getDirectionIndex(int degree) {
        double normalizedDegree = (degree % 360 + 360) % 360;

        return (int) Math.round(normalizedDegree / 45) % 8;
    }
}
