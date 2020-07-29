package ua.`in`.khol.oleh.touristweathercomparer

import org.junit.Assert
import org.junit.Test
import ua.`in`.khol.oleh.touristweathercomparer.model.cache.CacheHelper
import ua.`in`.khol.oleh.touristweathercomparer.model.cache.RxCacheHelper
import ua.`in`.khol.oleh.touristweathercomparer.model.location.LatLon

class ModelUnitTests {

    @Test
    fun whenPutLatLonInCacheHelper_returnSameValue() {
        val lat = 0.0
        val lon = 0.0
        val cacheHelper: CacheHelper = RxCacheHelper()
        val latLon = LatLon(lat, lon)

        cacheHelper.putLatLon(latLon)
        val storedLatLon = cacheHelper.latLon

        Assert.assertNotEquals(latLon, storedLatLon)
    }
}