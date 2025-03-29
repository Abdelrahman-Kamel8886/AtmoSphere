package com.abdok.atmosphere.Data.Local

import com.abdok.atmosphere.Data.Local.Room.LocalDao
import com.abdok.atmosphere.Data.Local.SharedPreference.ISharedPreferences
import com.abdok.atmosphere.Data.Models.AlertDTO
import com.abdok.atmosphere.Data.Models.FavouriteLocation
import com.abdok.atmosphere.Data.Models.HomeLocation
import com.abdok.atmosphere.Utils.Constants

class LocalDataSource private constructor(private val dao: LocalDao, private val sharedPreferences: ISharedPreferences) {

    //Room
    fun insertFavoriteLocation(favoriteLocation: FavouriteLocation) = dao.insertFavouriteLocation(favoriteLocation)
    fun deleteFavoriteLocation(favoriteLocation: FavouriteLocation) = dao.deleteFavouriteLocation(favoriteLocation)
    fun updateFavoriteLocation(favoriteLocation: FavouriteLocation) = dao.updateFavouriteLocation(favoriteLocation)
    fun getFavoriteLocations() = dao.getAllFavouriteLocations()

    fun insertAlert(alert: AlertDTO) = dao.insertAlert(alert)
    fun deleteAlert(alert: AlertDTO) = dao.deleteAlert(alert)
    fun getAlerts() = dao.getAllAlerts()

    fun updateHomeLocation(homeLocation: HomeLocation) = dao.updateHomeLocation(homeLocation)
    fun getHomeLocation() = dao.getHomeLocation()

    //SharedPreferences
    fun saveData(key: String, value: Any) = sharedPreferences.saveData(key, value)
    fun <T> fetchData(key: String, defaultValue: T) = sharedPreferences.fetchData(key, defaultValue)

    fun saveCurrentLocation(lat: Double , lon: Double){
        sharedPreferences.saveData(Constants.LOC_LAT , lat)
        sharedPreferences.saveData(Constants.LOC_LON , lon)
    }

    fun getCurrentLocation() : Pair<Double , Double>{
        val lat = sharedPreferences.fetchData<Double>(Constants.LOC_LAT , Constants.DEFAULT_LAT)
        val lon = sharedPreferences.fetchData(Constants.LOC_LON , Constants.DEFAULT_LON)
        return lat to lon
    }

    fun saveMapLocation(lat: Double , lon: Double){
        sharedPreferences.saveData(Constants.MAP_LAT , lat)
        sharedPreferences.saveData(Constants.MAP_LON , lon)
    }

    fun getMapLocation() : Pair<Double , Double>{
        val lat = sharedPreferences.fetchData(Constants.MAP_LAT , Constants.DEFAULT_LAT)
        val lon = sharedPreferences.fetchData(Constants.MAP_LON , Constants.DEFAULT_LON)
        return lat to lon
    }

    companion object {
        private var instance: LocalDataSource? = null
        fun getInstance(dao: LocalDao , sharedPreferences: ISharedPreferences): LocalDataSource {
            if (instance == null) {
                instance = LocalDataSource(dao , sharedPreferences)
            }
            return instance!!
        }
    }

}