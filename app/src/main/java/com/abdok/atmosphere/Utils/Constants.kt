package com.abdok.atmosphere.Utils

import com.abdok.atmosphere.BuildConfig
import com.abdok.atmosphere.Enums.Units


object Constants {



    const val BASE_URL = "https://api.openweathermap.org/"
    const val API_KEY = BuildConfig.API_KEY

    const val DATABASE_NAME = "local_db"
    const val FAVOURITE_TABLE = "favourite_table"

    const val DEFAULT_LANG = "ar"

    const val WEATHER_ENDPOINT = "data/2.5/weather"
    const val FORECAST_ENDPOINT = "data/2.5/forecast"

    const val GEO_ENDPOINT = "geo/1.0/direct"
    const val REVERSE_GEO_ENDPOINT = "geo/1.0/reverse"

    val degree = Units.METRIC.degree
    val unit = Units.METRIC.value




    val windUnit = "m/s"
    val visibilityUnit = "m"


}