package com.abdok.atmosphere.data.local

import com.abdok.atmosphere.data.local.room.LocalDao
import com.abdok.atmosphere.data.local.sharedPreference.ISharedPreferences
import com.abdok.atmosphere.data.models.AlertDTO
import com.abdok.atmosphere.data.models.FavouriteLocation
import com.abdok.atmosphere.data.models.HomeLocation
import com.abdok.atmosphere.utils.Constants

class LocalDataSourceImpl private constructor(private val dao: LocalDao, private val sharedPreferences: ISharedPreferences) : LocalDataSource {

    //Room
    override fun insertFavoriteLocation(favoriteLocation: FavouriteLocation) = dao.insertFavouriteLocation(favoriteLocation)
    override fun deleteFavoriteLocation(favoriteLocation: FavouriteLocation) = dao.deleteFavouriteLocation(favoriteLocation)
    override fun updateFavoriteLocation(favoriteLocation: FavouriteLocation) = dao.updateFavouriteLocation(favoriteLocation)
    override fun getFavoriteLocations() = dao.getAllFavouriteLocations()

    override fun insertAlert(alert: AlertDTO) = dao.insertAlert(alert)
    override fun deleteAlert(alert: AlertDTO) = dao.deleteAlert(alert)
    override fun deleteAlert(id: Int) = dao.deleteAlertById(id)
    override fun getAlerts() = dao.getAllAlerts()

    override fun updateHomeLocation(homeLocation: HomeLocation) = dao.updateHomeLocation(homeLocation)
    override fun getHomeLocation() = dao.getHomeLocation()

    //SharedPreferences
    override fun saveData(key: String, value: Any) = sharedPreferences.saveData(key, value)
    override fun <T> fetchData(key: String, defaultValue: T) = sharedPreferences.fetchData(key, defaultValue)

    override fun saveCurrentLocation(lat: Double , lon: Double){
        sharedPreferences.saveData(Constants.LOC_LAT , lat)
        sharedPreferences.saveData(Constants.LOC_LON , lon)
    }

    override fun getCurrentLocation() : Pair<Double , Double>{
        val lat = sharedPreferences.fetchData<Double>(Constants.LOC_LAT , Constants.DEFAULT_LAT)
        val lon = sharedPreferences.fetchData(Constants.LOC_LON , Constants.DEFAULT_LON)
        return lat to lon
    }

    override fun saveMapLocation(lat: Double , lon: Double){
        sharedPreferences.saveData(Constants.MAP_LAT , lat)
        sharedPreferences.saveData(Constants.MAP_LON , lon)
    }

    override fun getMapLocation() : Pair<Double , Double>{
        val lat = sharedPreferences.fetchData(Constants.MAP_LAT , Constants.DEFAULT_LAT)
        val lon = sharedPreferences.fetchData(Constants.MAP_LON , Constants.DEFAULT_LON)
        return lat to lon
    }

    companion object {
        private var instance: LocalDataSourceImpl? = null
        fun getInstance(dao: LocalDao , sharedPreferences: ISharedPreferences): LocalDataSourceImpl {
            if (instance == null) {
                instance = LocalDataSourceImpl(dao , sharedPreferences)
            }
            return instance!!
        }
    }

}