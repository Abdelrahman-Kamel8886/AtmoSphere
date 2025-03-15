package com.abdok.atmosphere.Data.DataSources

import com.abdok.atmosphere.Data.Remote.RetroServices

class RemoteDataSource private constructor(val service: RetroServices) {


    suspend fun getWeatherLatLon(
        lat: Double,
        lon: Double,
        units: String,
        lang: String
    ) = service.getWeatherLatLon(lat, lon, units, lang)

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