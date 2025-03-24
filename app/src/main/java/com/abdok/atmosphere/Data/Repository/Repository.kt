package com.abdok.atmosphere.Data.Repository

import com.abdok.atmosphere.Data.DataSources.LocalDataSource
import com.abdok.atmosphere.Data.DataSources.RemoteDataSource
import com.abdok.atmosphere.Data.Models.FavouriteLocation

class Repository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {

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

    suspend fun getCityLocation(cityName: String) = remoteDataSource.getCityLocation(cityName)

    suspend fun getCityName(lat: Double, lon: Double) = remoteDataSource.getCityName(lat, lon)

    fun insertFavoriteLocation(favorite: FavouriteLocation) = localDataSource.insertFavoriteLocation(favorite)
    fun deleteFavoriteLocation(favorite: FavouriteLocation) = localDataSource.deleteFavoriteLocation(favorite)
    fun getFavoriteLocations() = localDataSource.getFavoriteLocations()

    fun savePreferenceData(key: String, value: Any) = localDataSource.saveData(key, value)
    fun <T> fetchPreferenceData(key: String, defaultValue: T) = localDataSource.fetchData(key, defaultValue)


    companion object{
        private var instance: Repository? = null
        fun getInstance(remoteDataSource: RemoteDataSource , localDataSource: LocalDataSource): Repository {
            if (instance == null) {
                instance = Repository(remoteDataSource , localDataSource)
            }
            return instance!!
        }
    }

}