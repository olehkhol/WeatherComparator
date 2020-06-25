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
##### `GeocodingAuth.java`
```java
package ua.in.khol.oleh.touristweathercomparer.model.maps;
public class GeocodingAuth {
    // https://developers.google.com/maps/documentation/geocoding/get-api-key
    private static final String API_KEY = "API_KEY";
    
    public static String getApiKey() {
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

    public static String getApiKey() {
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
- [Navigation](https://developer.android.com/jetpack/androidx/releases/navigation) Version 2.2.2
- [Constraintlayout](https://developer.android.com/jetpack/androidx/releases/constraintlayout) Version 1.1.3
- [Appcompat](https://developer.android.com/jetpack/androidx/releases/appcompat) Version 1.3.0-alpha01
- [Vectordrawable](https://developer.android.com/jetpack/androidx/releases/vectordrawable) Version 1.1.0
- [Browser](https://developer.android.com/jetpack/androidx/releases/browser) Version 1.2.0
- [Exifinterface](https://developer.android.com/jetpack/androidx/releases/exifinterface) Version 1.2.0
- [Recyclerview](https://developer.android.com/jetpack/androidx/releases/recyclerview) Version 1.1.0
- [Cardview](https://developer.android.com/jetpack/androidx/releases/cardview) Version 1.0.0
- [Swiperefreshlayout](https://developer.android.com/jetpack/androidx/releases/swiperefreshlayout) Version 1.0.0
- [Preference](https://developer.android.com/jetpack/androidx/releases/preference) Version 1.1.1
- [Room](https://developer.android.com/jetpack/androidx/releases/room) Version 2.3.0-alpha01

ReactiveX
- [RxJava](https://github.com/ReactiveX/RxJava) Version 2.2.19
- [RxAndroid](https://github.com/ReactiveX/RxAndroid) Version 2.1.1

Square Open Source
- [OkHttp](https://square.github.io/okhttp) Version 3.12.8
- [Picasso](https://square.github.io/picasso) Version 2.71828
- [Retrofit](https://square.github.io/retrofit) Version 2.6.2
    - [RxJava2 Adapter](https://github.com/square/retrofit/tree/master/retrofit-adapters/rxjava2) Version 2.6.2
    - [Gson Converter](https://github.com/square/retrofit/tree/master/retrofit-converters/gson) Version 2.6.2
- [LeakCanary](https://square.github.io/leakcanary) Version 2.1

Google
- [Guava](https://github.com/google/guava) Version 28.2-android
- [Dagger](https://github.com/google/dagger) Version 2.24
- [Material](https://material.io/develop/android/docs/getting-started/) Version 1.1.0
- [Location](https://developer.android.com/training/location) Version 17.0.0
- [Maps](https://developer.android.com/training/maps) Version 17.0.0
- [Places](https://developers.google.com/places/android-sdk/intro) Version 2.3.0

Others
- [Conscrypt](https://github.com/google/conscrypt) Version 2.2.1
- [Joda-Time](https://www.joda.org/joda-time) Version 2.10.5
~~- [ReactiveNetwork](https://github.com/pwittchen/ReactiveNetwork) Version 3.0.7~~
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
