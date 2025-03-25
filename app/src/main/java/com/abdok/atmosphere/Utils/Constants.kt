package com.abdok.atmosphere.Utils

import com.abdok.atmosphere.BuildConfig
import com.abdok.atmosphere.Enums.Languages
import com.abdok.atmosphere.Enums.Units


object Constants {



    const val BASE_URL = "https://api.openweathermap.org/"
    const val API_KEY = BuildConfig.API_KEY

    const val DATABASE_NAME = "local_db"
    const val FAVOURITE_TABLE = "favourite_table"

    const val DEFAULT_LANG  = "en"

    const val WEATHER_ENDPOINT = "data/2.5/weather"
    const val FORECAST_ENDPOINT = "data/2.5/forecast"

    const val GEO_ENDPOINT = "geo/1.0/direct"
    const val REVERSE_GEO_ENDPOINT = "geo/1.0/reverse"

    //shared preference
    const val SHARED_PREFERENCE_NAME = "shared_preference"
    const val LANGUAGE_CODE = "language_code"
    const val TEMPERATURE_UNIT = "temperature_unit"
    const val WIND_SPEED_UNIT = "wind_speed_unit"
    const val LOCATION = "location"





    val windUnit = "m/s"


}