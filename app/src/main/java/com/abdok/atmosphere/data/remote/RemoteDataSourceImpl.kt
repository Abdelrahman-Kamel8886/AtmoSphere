package com.abdok.atmosphere.data.remote

import com.abdok.atmosphere.data.models.CityLocationResponseItem
import com.abdok.atmosphere.data.remote.retrofit.RetroServices
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class RemoteDataSourceImpl private constructor(private val service: RetroServices) : RemoteDataSource {


    override suspend fun getWeatherLatLon(
        lat: Double,
        lon: Double,
        units: String,
        lang: String
    ) = flowOf(service.getWeatherLatLon(lat, lon, units, lang))

    override suspend fun getForecastLatLon(
        lat: Double,
        lon: Double,
        units: String,
        lang: String
    ) = flowOf(service.getForecastLatLon(lat, lon, units, lang))


    override suspend fun getCityLocation(cityName: String) : Flow<List<CityLocationResponseItem>> {
        return flow {
            emit(service.getCityLocation(cityName))
        }
    }

    override suspend fun getCityName(lat: Double, lon: Double) : Flow<List<CityLocationResponseItem>> {
        return flow {
            emit(service.getCityName(lat, lon))
        }
    }

    companion object {
        private var instance: RemoteDataSourceImpl? = null
        fun getInstance(service: RetroServices): RemoteDataSourceImpl {
            if (instance == null) {
                instance = RemoteDataSourceImpl(service)
            }
            return instance!!
        }
    }

}