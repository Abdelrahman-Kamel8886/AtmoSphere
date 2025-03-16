package com.abdok.atmosphere.Utils

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object BackgroundMapper {

    val backgroundMap = mapOf(
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

    fun getBackground(condition: String): Brush {
        return backgroundMap[condition] ?: Brush.linearGradient(listOf(Color(0xFF000000), Color(0xFF434343))) // Default background
    }
}
