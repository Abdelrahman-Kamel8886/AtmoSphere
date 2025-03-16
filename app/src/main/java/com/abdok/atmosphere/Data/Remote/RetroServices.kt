package com.abdok.atmosphere.Data.Remote

import com.abdok.atmosphere.Data.Models.ForecastResponse
import com.abdok.atmosphere.Data.Models.WeatherResponse
import com.abdok.atmosphere.Utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface RetroServices {

    @GET(Constants.WEATHER_ENDPOINT)
    suspend fun getWeatherLatLon(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("lang") lang: String
    ): WeatherResponse

    @GET(Constants.FORECAST_ENDPOINT)
    suspend fun getForecastLatLon(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("lang") lang: String
        ): ForecastResponse
}