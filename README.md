# Weather Comparator 

The source code of an application
[Weather Comparator](https://play.google.com/store/apps/details?id=ua.in.khol.oleh.touristweathercomparer)

## Contents
* [Architecture](#architecture)
* [Services](#services)
* [Dependencies](#dependencies)
* [License](#license)

## Architecture

       MVVM + DATA BINDING + DI + RX

## Services

Location
- [Google Geocoding](https://developers.google.com/maps/documentation/geocoding)

Weather
- [Dark Sky](https://darksky.net/dev)
- [Yahoo Weather](https://developer.yahoo.com/weather)
- [OpenWeatherMap](https://openweathermap.org/api)
- [WorldWeatherOnline](https://www.worldweatheronline.com/developer/api)

## Dependencies

Android Architecture Components
- [Data Binding](https://developer.android.com/topic/libraries/data-binding/)
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
- [Room](https://developer.android.com/topic/libraries/architecture/room)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)

Square Open Source
- [OkHttp](https://square.github.io/okhttp) Version 3.12.3
- [Picasso](https://square.github.io/picasso) Version 2.71828
- [Retrofit](https://square.github.io/retrofit) Version 2.6.2
    - [RxJava2 Adapter](https://github.com/square/retrofit/tree/master/retrofit-adapters/rxjava2) Version 2.6.2
    - [Gson Converter](https://github.com/square/retrofit/tree/master/retrofit-converters/gson) Version 2.6.2
- [LeakCanary](https://square.github.io/leakcanary) Version 2.1

ReactiveX
- [RxJava](https://github.com/ReactiveX/RxJava) Version 2.2.17
- [RxAndroid](https://github.com/ReactiveX/RxAndroid) Version 2.1.1

Google
- [Guava](https://github.com/google/guava) Version 28.2-android
- [Dagger](https://github.com/google/dagger) Version 2.24
- [Material](https://material.io/develop/android/docs/getting-started/) Version 1.0.0
- [Location](https://developer.android.com/training/location) Version 17.0.0

Others
- [Joda-Time](https://www.joda.org/joda-time) Version 2.10.5
- [ReactiveNetwork](https://github.com/pwittchen/ReactiveNetwork) Version 3.0.6

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
