package com.abdok.atmosphere.utils.viewHelpers

import com.abdok.atmosphere.R

object GifBackground {

    private val gifMap = mapOf(
        "01d" to R.drawable.clear, // Clear sky (day)
        "02d" to R.drawable.few_clouds_day_gif, // Few clouds (day)
        "03d" to R.drawable.few_clouds_day_gif, // Scattered clouds (day)
        "04d" to R.drawable.few_clouds_day_gif, // Broken clouds (day)
        "09d" to R.drawable.rain_day_gif, // Shower rain (day)
        "10d" to R.drawable.rain_day_gif, // Rain (day)
        "11d" to R.drawable.thunder_gif, // Thunderstorm (day)"
        "13d" to R.drawable.snow_day_gif, // Snow (day)
        "50d" to R.drawable.mist_day_gif, // Mist (day)

        "01n" to R.drawable.clear_sky_night_gif, // Clear sky (night)
        "02n" to R.drawable.clouds_night_gif, // Few clouds (night)
        "03n" to R.drawable.clouds_night_gif, // Scattered clouds (night)
        "04n" to R.drawable.clouds_night_gif, // Broken clouds (night)
        "09n" to R.drawable.rain_night_gif, // Shower rain (night)
        "10n" to R.drawable.rain_night_gif, // Rain (night)
        "11n" to R.drawable.thunder_gif, // Thunderstorm (night)
        "13n" to R.drawable.snow_night_gif, // Snow (night)
        "50n" to R.drawable.mist_night_gif // Mist (night)



    )

    fun getGif(condition: String): Int {
        return gifMap[condition] ?: R.drawable.clear // Default background
    }

}