# The source code of an application [Weather Averages](https://play.google.com/store/apps/details?id=ua.in.khol.oleh.touristweathercomparer)


## Contents
* [Build](#build)
* [Architecture](#architecture)
* [Services](#services)
* [Dependencies](#dependencies)
* [License](#license)


## Build
### <span style="color:red;">You are free to use this code in any manner you see fit, but please refrain from creating direct clones of my application on Google Play Store or other platforms.</span>
### <span style="color:green;">Let's respect each other's creative work and innovation.</span>
### This is a fully functional codebase of the application. 
### The only thing you need to do is to fill the API keys file 
### and the resources file with your own values:

##### `app/src/main/java/ua/in/khol/oleh/touristweathercomparer/Secrets.java`
```java
package ua.in.khol.oleh.touristweathercomparer;

public interface Secrets {

    String OPEN_WEATHER_API_KEY = "YOUR_OPEN_WEATHER_API_KEY";
    String GEOCODING_API_KEY = "YOUR_GEOCODING_API_KEY";
    String PLACES_API_KEY = "YOUR_PLACES_API_KEY";
}
```

##### `app/src/main/res/values/api_keys.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="maps_sdk_for_android_api_key" translatable="false">YOUR_MAPS_SDK_FOR_ANDROID_API_KEY</string>
</resources>
```


## Architecture

       MVVM + DI + RX

In the Weather Averages application, ViewModel retrieval is handled uniquely in Activity and Fragment components. This process is critical to the MVVM architecture, ensuring that ViewModels are appropriately instantiated and tied to their respective lifecycle owners.

### ActivityView.getViewModelClass()

This method is used in the `ActivityView` class to dynamically determine the ViewModel class type associated with a specific activity. It employs Java generics and reflection to achieve this:
```java
package ua.in.khol.oleh.touristweathercomparer.ui;

public abstract class ActivityView<VM extends ActivityViewModel>
        extends AppCompatActivity
        implements ViewModelStoreOwner {
    
    private Class<VM> getViewModelClass() {
        Class<? extends ActivityView> aClass = getClass();
        Class superclass = aClass.getSuperclass();
        Type genericSuperclass = superclass.getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
        
        return (Class<VM>) actualTypeArgument;
    }
}
```

### FragmentView.getViewModelClass()

In `FragmentView`, the getViewModelClass method operates slightly differently, tailored for fragments:
```java
package ua.in.khol.oleh.touristweathercomparer.ui;

public abstract class FragmentView<VM extends FragmentViewModel>
        extends Fragment {

    private Class<VM> getViewModelClass() {
        Class<? extends FragmentView> aClass = getClass();
        Type genericSuperclass = aClass.getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
        
        return (Class<VM>) actualTypeArgument;
    }
}
```


## Services

Location API
- [Google Geocoding](https://developers.google.com/maps/documentation/geocoding)
- [Google Places](https://developers.google.com/maps/documentation/places)

Weather API
- [OpenWeatherMap](https://openweathermap.org/api)


## Dependencies

Android Architecture Components
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
- [Room](https://developer.android.com/topic/libraries/architecture/room)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)

AndroidX
- [Navigation](https://developer.android.com/jetpack/androidx/releases/navigation) Version 2.7.6
- [Constraintlayout](https://developer.android.com/jetpack/androidx/releases/constraintlayout) Version 2.7.6
- [Appcompat](https://developer.android.com/jetpack/androidx/releases/appcompat) Version 1.6.1
- [Recyclerview](https://developer.android.com/jetpack/androidx/releases/recyclerview) Version 1.3.2
- [Cardview](https://developer.android.com/jetpack/androidx/releases/cardview) Version 1.0.0
- [Preference](https://developer.android.com/jetpack/androidx/releases/preference) Version 1.2.1
- [Room](https://developer.android.com/jetpack/androidx/releases/room) Version 2.6.1

ReactiveX
- [RxJava](https://github.com/ReactiveX/RxJava) Version 2.2.21
- [RxAndroid](https://github.com/ReactiveX/RxAndroid) Version 2.1.1

Square Open Source
- [OkHttp](https://square.github.io/okhttp) Version 4.12.0
- [Picasso](https://square.github.io/picasso) Version 2.8
- [Retrofit](https://square.github.io/retrofit) Version 2.9.0
    - [RxJava2 Adapter](https://github.com/square/retrofit/tree/master/retrofit-adapters/rxjava2) Version 2.9.0
    - [Gson Converter](https://github.com/square/retrofit/tree/master/retrofit-converters/gson) Version 2.9.0
- [LeakCanary](https://square.github.io/leakcanary) Version 2.13

Google
- [Dagger](https://github.com/google/dagger) Version 2.50
- [Material](https://material.io/develop/android/docs/getting-started/) Version 1.11.0
- [Maps](https://developer.android.com/training/maps) Version 18.2.0
- [Places](https://developers.google.com/places/android-sdk) Version 3.3.0


## License

   Copyright 2020-2024 Oleh Kholiavchuk

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
