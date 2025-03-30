package com.abdok.atmosphere.data.local

import com.abdok.atmosphere.data.models.AlertDTO
import com.abdok.atmosphere.data.models.FavouriteLocation
import com.abdok.atmosphere.data.models.HomeLocation
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun insertFavoriteLocation(favoriteLocation: FavouriteLocation) : Long
    fun deleteFavoriteLocation(favoriteLocation: FavouriteLocation) : Int
    fun updateFavoriteLocation(favoriteLocation: FavouriteLocation) : Int
    fun getFavoriteLocations() : Flow<List<FavouriteLocation>>

    fun insertAlert(alert: AlertDTO) :Long
    fun deleteAlert(alert: AlertDTO) :Int
    fun deleteAlert(id: Int) :Int
    fun getAlerts() : Flow<List<AlertDTO>>

    fun updateHomeLocation(homeLocation: HomeLocation) : Long
    fun getHomeLocation(): Flow<HomeLocation>

    fun saveData(key: String, value: Any)
    fun <T> fetchData(key: String, defaultValue: T): T

    fun saveCurrentLocation(lat: Double , lon: Double)
    fun getCurrentLocation() : Pair<Double , Double>
    fun saveMapLocation(lat: Double , lon: Double)
    fun getMapLocation() : Pair<Double , Double>
}