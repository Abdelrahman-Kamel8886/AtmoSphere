package com.abdok.atmosphere.data.remote.retrofit

import com.abdok.atmosphere.data.models.CityLocationResponseItem
import com.abdok.atmosphere.data.models.ForecastResponse
import com.abdok.atmosphere.data.models.WeatherResponse
import com.abdok.atmosphere.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface RetroServices {

    @GET(Constants.WEATHER_ENDPOINT)
    suspend fun getWeatherLatLon(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("lang") lang: String):
            WeatherResponse

    @GET(Constants.FORECAST_ENDPOINT)
    suspend fun getForecastLatLon(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("lang") lang: String
        ): ForecastResponse

    @GET(Constants.GEO_ENDPOINT)
    suspend fun getCityLocation(
        @Query("q") cityName: String)  : List<CityLocationResponseItem>

    @GET(Constants.REVERSE_GEO_ENDPOINT)
    suspend fun getCityName(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
    ) : List<CityLocationResponseItem>
}