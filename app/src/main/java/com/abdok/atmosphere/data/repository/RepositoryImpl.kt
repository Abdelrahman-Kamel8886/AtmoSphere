package com.abdok.atmosphere.data.repository

import com.abdok.atmosphere.data.local.LocalDataSource
import com.abdok.atmosphere.data.local.LocalDataSourceImpl
import com.abdok.atmosphere.data.models.AlertDTO
import com.abdok.atmosphere.data.remote.RemoteDataSourceImpl
import com.abdok.atmosphere.data.models.FavouriteLocation
import com.abdok.atmosphere.data.models.HomeLocation
import com.abdok.atmosphere.data.remote.RemoteDataSource
import com.abdok.atmosphere.enums.Locations
import com.abdok.atmosphere.utils.Constants

class RepositoryImpl private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : Repository {

    override suspend fun getWeatherLatLon(
        lat: Double,
        lon: Double,
        units: String,
        lang: String) = remoteDataSource.getWeatherLatLon(lat, lon, units, lang)

    override suspend fun getForecastLatLon(
        lat: Double,
        lon: Double,
        units: String,
        lang: String) = remoteDataSource.getForecastLatLon(lat, lon, units, lang)

    override suspend fun getCityLocation(cityName: String) = remoteDataSource.getCityLocation(cityName)

    override suspend fun getCityName(lat: Double, lon: Double) = remoteDataSource.getCityName(lat, lon)

    override fun getFavoriteLocations() = localDataSource.getFavoriteLocations()
    override fun insertFavoriteLocation(favorite: FavouriteLocation) = localDataSource.insertFavoriteLocation(favorite)
    override fun deleteFavoriteLocation(favorite: FavouriteLocation) = localDataSource.deleteFavoriteLocation(favorite)
    override fun updateFavoriteLocation(favorite: FavouriteLocation) = localDataSource.updateFavoriteLocation(favorite)

    override fun updateHomeLocation(homeLocation: HomeLocation) = localDataSource.updateHomeLocation(homeLocation)
    override fun getHomeLocation() = localDataSource.getHomeLocation()

    override fun insertAlert(alert: AlertDTO) = localDataSource.insertAlert(alert)
    override fun deleteAlert(alert: AlertDTO) = localDataSource.deleteAlert(alert)
    override fun deleteAlert(id: Int) = localDataSource.deleteAlert(id)
    override fun getAlerts() = localDataSource.getAlerts()

    override fun savePreferenceData(key: String, value: Any) = localDataSource.saveData(key, value)
    override fun <T> fetchPreferenceData(key: String, defaultValue: T) = localDataSource.fetchData(key, defaultValue)

    override fun getLocation():Pair<Double , Double>{
        val pref =fetchPreferenceData(Constants.LOCATION , Locations.Gps.value)
        if(pref == Locations.Map.value){
            return localDataSource.getMapLocation()
        }
        return localDataSource.getCurrentLocation()
    }

    override fun saveLocation(locationType : Locations, lat: Double, lon: Double){
        if(locationType == Locations.Gps){
            localDataSource.saveCurrentLocation(lat, lon)
        }
        else{
            localDataSource.saveMapLocation(lat, lon)
        }
    }



    companion object{
        private var instance: RepositoryImpl? = null
        fun getInstance(remoteDataSourceImpl: RemoteDataSourceImpl, localDataSourceImpl: LocalDataSourceImpl): RepositoryImpl {
            if (instance == null) {
                instance = RepositoryImpl(remoteDataSourceImpl , localDataSourceImpl)
            }
            return instance!!
        }
    }

}