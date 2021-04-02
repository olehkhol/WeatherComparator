package ua.in.khol.oleh.touristweathercomparer.model.maps;

import org.junit.Test;

import static org.junit.Assert.*;

public class TimeZoneAuthTest {
    @Test
    public void zdzd(){
        String key = TimeZoneAuth.getTimeZoneApiKey();
        System.out.println(key);
    }
}