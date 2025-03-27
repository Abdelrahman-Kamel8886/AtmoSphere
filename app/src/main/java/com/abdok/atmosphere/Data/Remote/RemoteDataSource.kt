package com.abdok.atmosphere.Data.Remote

import com.abdok.atmosphere.Data.Models.CityLocationResponseItem
import com.abdok.atmosphere.Data.Remote.Retrofit.RetroServices
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteDataSource private constructor(val service: RetroServices) {


    suspend fun getWeatherLatLon(
        lat: Double,
        lon: Double,
        units: String,
        lang: String
    ) = service.getWeatherLatLon(lat, lon, units, lang)

    suspend fun getForecastLatLon(
        lat: Double,
        lon: Double,
        units: String,
        lang: String
    ) = service.getForecastLatLon(lat, lon, units, lang)


    suspend fun getCityLocation(cityName: String) : Flow<List<CityLocationResponseItem>> {
        return flow {
            emit(service.getCityLocation(cityName))
        }
    }

    suspend fun getCityName(lat: Double, lon: Double) : Flow<List<CityLocationResponseItem>> {
        return flow {
            emit(service.getCityName(lat, lon))
        }
    }

    companion object {
        private var instance: RemoteDataSource? = null
        fun getInstance(service: RetroServices): RemoteDataSource {
            if (instance == null) {
                instance = RemoteDataSource(service)
            }
            return instance!!
        }
    }

}