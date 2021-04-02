# Weather Averages

###### The source code of an application
[Weather Averages](https://play.google.com/store/apps/details?id=ua.in.khol.oleh.touristweathercomparer)

## Contents
* [Build](#build)
* [Architecture](#architecture)
* [Services](#services)
* [Dependencies](#dependencies)
* [License](#license)

## Build
###### Add four classes containing your own private API keys to the correspond packages
##### `PlacesAuth.java`
```java
package ua.in.khol.oleh.touristweathercomparer.model.places;
public class PlacesAuth {
    // https://developers.google.com/maps/documentation/places/web-service/get-api-key
    private static final String API_KEY = "API_KEY";
    
    public static String getPlacesApiKey() {
        return API_KEY;
    }
}
```

##### `GeocodingAuth.java`
```java
package ua.in.khol.oleh.touristweathercomparer.model.maps;
public class GeocodingAuth {
    // https://developers.google.com/maps/documentation/geocoding/get-api-key
    private static final String API_KEY = "API_KEY";
    
    public static String getGeocodingApiKey() {
        return API_KEY;
    }
}
```

##### `TimeZoneAuth.java`
```java
package ua.in.khol.oleh.touristweathercomparer.model.maps;
class TimeZoneAuth {
    // https://developers.google.com/maps/documentation/timezone/get-api-key
    private static final String API_KEY = "API_KEY";

    public static String getTimeZoneApiKey() {
        return API_KEY;
    }
}
```

##### `DarkSkyAuth.java`
```java
package ua.in.khol.oleh.touristweathercomparer.model.weather.darksky;
public class DarkSkyAuth {
    // https://darksky.net/dev/account
    private static final String SECRET_KEY = "SECRET_KEY";
    
    public static String getSecretKey() {
        return SECRET_KEY;
    }
}
```
##### `OwmAuth.java`
```java
package ua.in.khol.oleh.touristweathercomparer.model.weather.owm;
public class OwmAuth {
    // https://home.openweathermap.org/api_keys
    private static final String API_KEY = "API_KEY";

    public static String getApiKey() {
        return API_KEY;
    }
}
```

## Architecture

       MVVM + DATA BINDING + DI + RX

## Services

Location
- [Google Geocoding](https://developers.google.com/maps/documentation/geocoding)
- [Google Time Zone](https://developers.google.com/maps/documentation/timezone)

Weather
- [Dark Sky](https://darksky.net/dev)
- [OpenWeatherMap](https://openweathermap.org/api)

## Dependencies

Android Architecture Components
- [Data Binding](https://developer.android.com/topic/libraries/data-binding/)
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
- [Room](https://developer.android.com/topic/libraries/architecture/room)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)

AndroidX
- [Multidex](https://developer.android.com/studio/build/multidex) Version 2.0.1
- [Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle) Version 2.2.0
- [Navigation](https://developer.android.com/jetpack/androidx/releases/navigation) Version 2.3.4
- [Constraintlayout](https://developer.android.com/jetpack/androidx/releases/constraintlayout) Version 2.0.4
- [Appcompat](https://developer.android.com/jetpack/androidx/releases/appcompat) Version 1.3.0-rc01
- [Vectordrawable](https://developer.android.com/jetpack/androidx/releases/vectordrawable) Version 1.1.0
- [Browser](https://developer.android.com/jetpack/androidx/releases/browser) Version 1.3.0
- [Exifinterface](https://developer.android.com/jetpack/androidx/releases/exifinterface) Version 1.3.2
- [Recyclerview](https://developer.android.com/jetpack/androidx/releases/recyclerview) Version 1.1.0
- [Cardview](https://developer.android.com/jetpack/androidx/releases/cardview) Version 1.0.0
- [Swiperefreshlayout](https://developer.android.com/jetpack/androidx/releases/swiperefreshlayout) Version 1.1.0
- [Preference](https://developer.android.com/jetpack/androidx/releases/preference) Version 1.1.1
- [Room](https://developer.android.com/jetpack/androidx/releases/room) Version 2.2.6

ReactiveX
- [RxJava](https://github.com/ReactiveX/RxJava) Version 2.2.21
- [RxAndroid](https://github.com/ReactiveX/RxAndroid) Version 2.1.1

Square Open Source
- [OkHttp](https://square.github.io/okhttp) Version 3.12.8
- [Picasso](https://square.github.io/picasso) Version 2.71828
- [Retrofit](https://square.github.io/retrofit) Version 2.6.4
    - [RxJava2 Adapter](https://github.com/square/retrofit/tree/master/retrofit-adapters/rxjava2) Version 2.6.4
    - [Gson Converter](https://github.com/square/retrofit/tree/master/retrofit-converters/gson) Version 2.6.4
- [LeakCanary](https://square.github.io/leakcanary) Version 2.5

Google
- [Guava](https://github.com/google/guava) Version 28.2-android
- [Dagger](https://github.com/google/dagger) Version 2.29.1
- [Material](https://material.io/develop/android/docs/getting-started/) Version 1.3.0
- [Location](https://developer.android.com/training/location) Version 18.0.0
- [Maps](https://developer.android.com/training/maps) Version 17.0.0
- [Places](https://developers.google.com/places/android-sdk/intro) Version 2.4.0

Others
- [Conscrypt](https://github.com/google/conscrypt) Version 2.2.1
- [Joda-Time](https://www.joda.org/joda-time) Version 2.10.6
- [Timber](https://github.com/JakeWharton/timber) Version 4.7.1

## License

   Copyright 2020 Oleh Kholiavchuk

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
