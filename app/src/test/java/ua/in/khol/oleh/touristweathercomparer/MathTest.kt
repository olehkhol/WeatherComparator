package ua.`in`.khol.oleh.touristweathercomparer

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import ua.`in`.khol.oleh.touristweathercomparer.utils.Math

private const val ZERO: Double = 0.0;
private const val PLACES: Int = 0;

@RunWith(JUnit4::class)
class MathTest {
    @Test
    fun whenRoundZero_shouldReturnZero() {
        var data: Double = Math.round(ZERO, PLACES);

        if (data == 0.0)
            print("Success")
        else
            throw AssertionError("Invalid round")
    }
}
