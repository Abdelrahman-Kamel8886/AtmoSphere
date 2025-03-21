package com.abdok.atmosphere.Data.Repository

import com.abdok.atmosphere.Data.DataSources.RemoteDataSource

class Repository private constructor(private val remoteDataSource: RemoteDataSource) {

    suspend fun getWeatherLatLon(
        lat: Double,
        lon: Double,
        units: String,
        lang: String) = remoteDataSource.getWeatherLatLon(lat, lon, units, lang)

    suspend fun getForecastLatLon(
        lat: Double,
        lon: Double,
        units: String,
        lang: String) = remoteDataSource.getForecastLatLon(lat, lon, units, lang)

    companion object{
        private var instance: Repository? = null
        fun getInstance(remoteDataSource: RemoteDataSource): Repository {
            if (instance == null) {
                instance = Repository(remoteDataSource)
            }
            return instance!!
        }
    }

}