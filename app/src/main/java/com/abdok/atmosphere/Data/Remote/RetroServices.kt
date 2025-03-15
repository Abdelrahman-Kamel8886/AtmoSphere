package com.abdok.atmosphere.Data.Remote

import com.abdok.atmosphere.Data.Models.WeatherResponse
import com.abdok.atmosphere.Enums.Units
import retrofit2.http.GET
import retrofit2.http.Query

interface RetroServices {

    @GET("Weather")
    suspend fun getWeatherLatLon(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("lang") lang: String
    ): WeatherResponse


}