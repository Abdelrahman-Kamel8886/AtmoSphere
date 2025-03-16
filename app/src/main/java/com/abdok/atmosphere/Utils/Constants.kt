package com.abdok.atmosphere.Utils

import com.abdok.atmosphere.BuildConfig
import com.abdok.atmosphere.Enums.Units


object Constants {

    const val BASE_URL = "https://api.openweathermap.org/"
    const val API_KEY = BuildConfig.API_KEY

    const val DEFAULT_LANG = "en"

    const val WEATHER_ENDPOINT = "data/2.5/weather"
    const val FORECAST_ENDPOINT = "data/2.5/forecast"

    val degree = Units.METRIC.degree
    val unit = Units.METRIC.value


}