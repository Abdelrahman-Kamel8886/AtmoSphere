package com.abdok.atmosphere.data.remote

import com.abdok.atmosphere.data.models.CityLocationResponseItem
import com.abdok.atmosphere.data.models.ForecastResponse
import com.abdok.atmosphere.data.models.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    suspend fun getWeatherLatLon(lat: Double, lon: Double, units: String, lang: String) : Flow<WeatherResponse>
    suspend fun getForecastLatLon(lat: Double, lon: Double, units: String, lang: String) : Flow<ForecastResponse>
    suspend fun getCityLocation(cityName: String) : Flow<List<CityLocationResponseItem>>
    suspend fun getCityName(lat: Double, lon: Double) : Flow<List<CityLocationResponseItem>>

}