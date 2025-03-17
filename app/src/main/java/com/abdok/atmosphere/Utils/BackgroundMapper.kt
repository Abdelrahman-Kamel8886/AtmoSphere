package com.abdok.atmosphere.Utils

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object BackgroundMapper {

    val cardBackgroundMap = mapOf(
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

    val screenBackgroundMap = mapOf(
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
}
