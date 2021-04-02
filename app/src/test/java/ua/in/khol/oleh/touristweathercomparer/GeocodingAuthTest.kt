package ua.`in`.khol.oleh.touristweathercomparer

import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.slot
import org.junit.Before
import org.junit.Test
import ua.`in`.khol.oleh.touristweathercomparer.model.maps.GeocodingAuth

class GeocodingAuthTest {

    @Before
    fun `Bypass android_util_Base64 to java_util_Base64`() {
        mockkStatic(android.util.Base64::class)
        val arraySlot = slot<ByteArray>()

        every {
            android.util.Base64.encodeToString(capture(arraySlot), android.util.Base64.DEFAULT)
        } answers {
            java.util.Base64.getEncoder().encodeToString(arraySlot.captured)
        }

        val stringSlot = slot<String>()
        every {
            android.util.Base64.decode(capture(stringSlot), android.util.Base64.DEFAULT)
        } answers {
            java.util.Base64.getDecoder().decode(stringSlot.captured)
        }
    }

    // Test .getApiKey() returns not null
    @Test
    fun checkGeocodingApiKey_returnsNotNull() {
        val key: String = GeocodingAuth.getGeocodingApiKey();

        if (key != null)
            print("Success")
        else
            throw AssertionError("Geocoding API_KEY is null")
    }

    /**
     * Checks the correctness of Base64 data
     */
    private fun isBase64Encoded(data: String): Boolean {
        val regex = Regex("""^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$""")

        return regex.containsMatchIn(data);
    }

    // Test .getApiKey() returns not empty string
    @Test
    fun checkGeocodingApiKey_returnsNotEmpty() {
        val key: String = GeocodingAuth.getGeocodingApiKey();

        if (key.isNotEmpty())
            print("Success")
        else
            throw AssertionError("Geocoding API_KEY is empty")
    }


}