package com.abdok.atmosphere.data

import com.abdok.atmosphere.data.local.LocalDataSource
import com.abdok.atmosphere.data.models.AlertDTO
import com.abdok.atmosphere.data.remote.RemoteDataSource
import com.abdok.atmosphere.data.models.FavouriteLocation
import com.abdok.atmosphere.data.models.HomeLocation
import com.abdok.atmosphere.enums.Locations
import com.abdok.atmosphere.utils.Constants

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

    fun getFavoriteLocations() = localDataSource.getFavoriteLocations()
    fun insertFavoriteLocation(favorite: FavouriteLocation) = localDataSource.insertFavoriteLocation(favorite)
    fun deleteFavoriteLocation(favorite: FavouriteLocation) = localDataSource.deleteFavoriteLocation(favorite)
    fun updateFavoriteLocation(favorite: FavouriteLocation) = localDataSource.updateFavoriteLocation(favorite)

    fun updateHomeLocation(homeLocation: HomeLocation) = localDataSource.updateHomeLocation(homeLocation)
    fun getHomeLocation() = localDataSource.getHomeLocation()

    fun insertAlert(alert: AlertDTO) = localDataSource.insertAlert(alert)
    fun deleteAlert(alert: AlertDTO) = localDataSource.deleteAlert(alert)
    fun deleteAlert(id: Int) = localDataSource.deleteAlert(id)
    fun getAlerts() = localDataSource.getAlerts()

    fun savePreferenceData(key: String, value: Any) = localDataSource.saveData(key, value)
    fun <T> fetchPreferenceData(key: String, defaultValue: T) = localDataSource.fetchData(key, defaultValue)

    fun getLocation():Pair<Double , Double>{
        val pref =fetchPreferenceData(Constants.LOCATION , Locations.Gps.value)
        if(pref == Locations.Map.value){
            return localDataSource.getMapLocation()
        }
        return localDataSource.getCurrentLocation()
    }

    fun saveLocation(locationType : Locations , lat: Double, lon: Double){
        if(locationType == Locations.Gps){
            localDataSource.saveCurrentLocation(lat, lon)
        }
        else{
            localDataSource.saveMapLocation(lat, lon)
        }
    }



    companion object{
        private var instance: Repository? = null
        fun getInstance(remoteDataSource: RemoteDataSource, localDataSource: LocalDataSource): Repository {
            if (instance == null) {
                instance = Repository(remoteDataSource , localDataSource)
            }
            return instance!!
        }
    }

}