package com.abdok.atmosphere.utils.viewHelpers

import com.abdok.atmosphere.R

object IconsMapper {

    val iconsMap = mapOf(
        "01d" to R.drawable.clear_sky_day,
        "02d" to R.drawable.few_clouds_day,
        "03d" to R.drawable.scattered_clouds_day,
        "04d" to R.drawable.broken_clouds_day,
        "09d" to R.drawable.shower_rain_day,
        "10d" to R.drawable.rain_day,
        "11d" to R.drawable.thunderstorm_day,
        "13d" to R.drawable.snow_day,
        "50d" to R.drawable.mist_day,

        "01n" to R.drawable.clear_sky_night,
        "02n" to R.drawable.few_clouds_night,
        "03n" to R.drawable.scattered_clouds_night,
        "04n" to R.drawable.scattered_clouds_night,
        "09n" to R.drawable.shower_rain_night,
        "10n" to R.drawable.rain_night,
        "11n" to R.drawable.thunderstorm_day,
        "13n" to R.drawable.snow_night,
        "50n" to R.drawable.mist_night
    )

    fun getIcon(iconCode: String): Int {
        return iconsMap[iconCode] ?: R.drawable.clear_sky_day
    }

}