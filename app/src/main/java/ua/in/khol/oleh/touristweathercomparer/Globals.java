package ua.in.khol.oleh.touristweathercomparer;

public interface Globals {

    int CORE_POOL_SIZE = 3;
    int MAXIMUM_POOL_SIZE = 5;
    long KEEP_ALIVE_TIME = 10;

    long REQUEST_TIMEOUT = 1;
    long MILLIS_IN_SECOND = 1000L;

    int MENU_ITEM_SEARCH = 1;
    int MENU_ITEM_SETTINGS = 3;

    String PATH_SEPARATOR = "/";
    String DB_FILE_NAME = "wa.db";

    String PLACE = "PLACE";
    String LOCATION_FORMAT = "( %2.4f, %2.4f )";
    String GEOLOCATION_FORMAT = "geo:%f,%f";

    String MAPS_GOOGLEAPIS = "https://maps.googleapis.com";

    String OPEN_WEATHER_NAME = "OpenWeather";
    String OPEN_WEATHER_SITE = "https://openweathermap.org/";
    String OPEN_WEATHER_API_URL = "http://api.openweathermap.org";
}