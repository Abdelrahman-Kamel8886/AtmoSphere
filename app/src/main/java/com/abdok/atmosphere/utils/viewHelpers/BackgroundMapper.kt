package com.abdok.atmosphere.utils.viewHelpers

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

object BackgroundMapper {

    private val lightColors = mapOf(
        "01d" to Color(0xFFFFE082), // Clear sky (day)
        "02d" to Color(0xFF304352), // Few clouds (day)
        "03d" to Color(0xFFD7DDE8), // Scattered clouds (day)
        "04d" to Color(0xFF4CA1AF), // Broken clouds (day)
        "09d" to Color(0xFF00C6FB), // Shower rain (day)
        "10d" to Color(0xFF90CAF9), // Rain (day)
        "11d" to Color(0xFF434343), // Thunderstorm (day)
        "13d" to Color(0xFFb6fbff), // Snow (day)
        "50d" to Color(0xFF2c3e50), // Mist (day)

        "01n" to Color(0xFF232526), // Clear sky (night)
        "02n" to Color(0xFF2C5364), // Few clouds (night)
        "03n" to Color(0xFF243B55),
        "04n" to Color(0xFF4286F4),
        "09n" to Color(0xFF00C6FB),
        "10n" to Color(0xFF000000),
        "11n" to Color(0xFF434343),
        "13n" to Color(0xFFb6fbff),
        "50n" to Color(0xFF2c3e50)

    )

    private val mainTextColorMap = mapOf(
        "01d" to Color.White, // Clear sky (day)
        "02d" to Color.DarkGray, // Few clouds (day)
        "09d" to Color.White, // Shower rain (day)"
        "10d" to Color.White, // Rain (day)
        "11d" to Color.White, // Thunderstorm (day)



        "01n" to Color.White, // Clear sky (Night)
        "02n" to Color.White, // Few clouds (Night)
        "03n" to Color.White, // Scattered clouds (Night)
        "04n" to Color.White, // Broken clouds (Night)
        "09n" to Color.White, // Shower rain (Night)
        "10n" to Color.White, // Rain (Night)
        "11n" to Color.White, // Thunderstorm (Night)
        "13n" to Color.White, // Snow (Night)
        "50n" to Color.White // Mist (Night)


    )

    private val HourTempTextColorMap = mapOf(
        "01d" to (Color.DarkGray to Color.Black), // Clear sky (day)
        "02d" to (Color.DarkGray to Color.Black), // Few clouds (day)
        "03d" to (Color.DarkGray to Color.Black), // Scattered clouds (day)
        "04d" to (Color.DarkGray to Color.Black), // Broken clouds (day)
        "09d" to (Color.DarkGray to Color.Black), // Shower rain (day)
        "10d" to (Color.DarkGray to Color.Black), // Rain (day)
        "11d" to (Color.LightGray to Color.White), // Thunderstorm (day)
        "13d" to (Color.DarkGray to Color.Black), // Snow (day)
        "50d" to (Color.DarkGray to Color.Black), // Mist (day)


        "01n" to (Color.LightGray to Color.White), // Clear sky (Night)
        "02n" to (Color.LightGray to Color.White), // Few clouds (Night)
        "03n" to (Color.LightGray to Color.White), // Scattered clouds (Night)
        "04n" to (Color.LightGray to Color.White), // Broken clouds (Night)
        "09n" to (Color.LightGray to Color.White), // Shower rain (Night)
        "10n" to (Color.LightGray to Color.White), // Rain (Night)
        "11n" to (Color.LightGray to Color.White), // Thunderstorm (Night)
        "13n" to (Color.LightGray to Color.White), // Snow (Night)
        "50n" to (Color.LightGray to Color.White) // Mist (Night)

    )


    private val cardBackgroundMap = mapOf(
        "01d" to Brush.linearGradient(listOf(Color(0xFFFFE082), Color(0xFFFFCA28))), // Clear sky (day)
        "02d" to Brush.linearGradient(listOf(Color(0xFFd7d2cc), Color(0xFF304352))), // Few clouds (day)
        "03d" to Brush.linearGradient(listOf(Color(0xFF757F9A), Color(0xFFD7DDE8))), // Scattered clouds (day)
        "04d" to Brush.linearGradient(listOf(Color(0xFF2C3E50), Color(0xFF4CA1AF))), // Broken clouds (day)
        "09d" to Brush.linearGradient(listOf(Color(0xFF00C6FB), Color(0xFF005BEA))), // Shower rain (day)
        "10d" to Brush.linearGradient(listOf(Color(0xFF90CAF9), Color(0xFF64B5F6))), // Rain (day)
        "11d" to Brush.linearGradient(listOf(Color(0xFF000000), Color(0xFF434343))), // Thunderstorm (day)
        "13d" to Brush.linearGradient(listOf(Color(0xFF83a4d4), Color(0xFFb6fbff))), // Snow (day)
        "50d" to Brush.linearGradient(listOf(Color(0xFFbdc3c7), Color(0xFF2c3e50))), // Mist (day)


        "01n" to Brush.linearGradient(listOf(Color(0xFF232526), Color(0xFF414345))), // Clear sky (night)
        "02n" to Brush.linearGradient(listOf(Color(0xFF2C5364), Color(0xFF0F2027))), // Few clouds (night)
        "03n" to Brush.linearGradient(listOf(Color(0xFF141E30), Color(0xFF243B55))), // Scattered clouds (night)
        "04n" to Brush.linearGradient(listOf(Color(0xFF373B44), Color(0xFF4286F4))), // Broken clouds (night)
        "09n" to Brush.linearGradient(listOf(Color(0xFF00C6FB), Color(0xFF005BEA))), // Shower rain (night)
        "10n" to Brush.linearGradient(listOf(Color(0xFF000000), Color(0xFF434343))), // Rain (night)
        "11n" to Brush.linearGradient(listOf(Color(0xFF232526), Color(0xFF414345))), // Thunderstorm (night)
        "13n" to Brush.linearGradient(listOf(Color(0xFF83a4d4), Color(0xFFb6fbff))), // Snow (night)
        "50n" to Brush.linearGradient(listOf(Color(0xFFbdc3c7), Color(0xFF2c3e50)))  // Mist (night)
    )

    private val screenBackgroundMap = mapOf(
        "01d" to Brush.verticalGradient(listOf(Color(0xFFFFF3E0), Color(0xFFFFE082))), // Clear sky (day)
        "02d" to Brush.verticalGradient(listOf(Color(0xFFD3CBB8), Color(0xFF606c88))), // Few clouds (day)
        "03d" to Brush.verticalGradient(listOf(Color(0xFFa1c4fd), Color(0xFFc2e9fb))), // Scattered clouds (day)
        "04d" to Brush.verticalGradient(listOf(Color(0xFF667db6), Color(0xFF0082c8))), // Broken clouds (day)
        "09d" to Brush.verticalGradient(listOf(Color(0xFF00d2ff), Color(0xFF3a7bd5))), // Shower rain (day)
        "10d" to Brush.verticalGradient(listOf(Color(0xFF90CAF9), Color(0xFF64B5F6))), // Rain (day)
        "11d" to Brush.verticalGradient(listOf(Color(0xFF232526), Color(0xFF414345))), // Thunderstorm (day)
        "13d" to Brush.verticalGradient(listOf(Color(0xFFece9e6), Color(0xFFffffff))), // Snow (day)
        "50d" to Brush.verticalGradient(listOf(Color(0xFFbdc3c7), Color(0xFF2c3e50))), // Mist (day)

        "01n" to Brush.verticalGradient(listOf(Color(0xFF232526), Color(0xFF414345))), // Clear sky (night)
        "02n" to Brush.verticalGradient(listOf(Color(0xFF1e3c72), Color(0xFF2a5298))), // Few clouds (night)
        "03n" to Brush.verticalGradient(listOf(Color(0xFF141E30), Color(0xFF243B55))), // Scattered clouds (night)
        "04n" to Brush.verticalGradient(listOf(Color(0xFF373B44), Color(0xFF4286F4))), // Broken clouds (night)
        "09n" to Brush.verticalGradient(listOf(Color(0xFF00c6ff), Color(0xFF0072ff))), // Shower rain (night)
        "10n" to Brush.verticalGradient(listOf(Color(0xFF000000), Color(0xFF434343))), // Rain (night)
        "11n" to Brush.verticalGradient(listOf(Color(0xFF232526), Color(0xFF414345))), // Thunderstorm (night)
        "13n" to Brush.verticalGradient(listOf(Color(0xFFc9d6ff), Color(0xFFe2e2e2))), // Snow (night)
        "50n" to Brush.verticalGradient(listOf(Color(0xFFbdc3c7), Color(0xFF2c3e50)))  // Mist (night)
    )

    fun getCardBackground(condition: String): Brush {
        return cardBackgroundMap[condition] ?: Brush.linearGradient(listOf(Color(0xFF000000), Color(0xFF434343))) // Default background
    }
    fun getScreenBackground(condition: String): Brush {
        return screenBackgroundMap[condition] ?: Brush.linearGradient(listOf(Color(0xFF000000), Color(0xFF434343))) // Default background
    }

    fun getTextColor(condition: String): Color {
        val color = lightColors[condition] ?: Color.DarkGray
        return getContrastingTextColor(color)
    }

    fun getMainTextColor(condition: String): Color {
        return mainTextColorMap[condition] ?: Color.DarkGray
    }

    fun getHourTempTextColor(condition: String): Pair<Color, Color> {
        return HourTempTextColorMap[condition] ?: (Color.Gray to Color.DarkGray)
    }

    private fun getContrastingTextColor(bgColor: Color): Color {
        val luminance = bgColor.luminance()
        return if (luminance < 0.5) Color.White else Color.DarkGray
    }
}
