package com.abdok.atmosphere.data.repository

import com.abdok.atmosphere.data.local.LocalDataSource
import com.abdok.atmosphere.data.models.AlertDTO
import com.abdok.atmosphere.data.models.FavouriteLocation
import com.abdok.atmosphere.data.models.HomeLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeLocalDataSource(private val alerts : MutableList<AlertDTO> = mutableListOf()) : LocalDataSource {


    override fun insertFavoriteLocation(favoriteLocation: FavouriteLocation): Long {
        TODO("Not yet implemented")
    }

    override fun deleteFavoriteLocation(favoriteLocation: FavouriteLocation): Int {
        TODO("Not yet implemented")
    }

    override fun updateFavoriteLocation(favoriteLocation: FavouriteLocation): Int {
        TODO("Not yet implemented")
    }

    override fun getFavoriteLocations(): Flow<List<FavouriteLocation>> {
        TODO("Not yet implemented")
    }

    override fun insertAlert(alert: AlertDTO): Long {
        if(!alerts.contains(alert)){
            alerts.add(alert)
            return 1L
        }
        return 0L
    }

    override fun deleteAlert(alert: AlertDTO): Int {
        if(alerts.contains(alert)){
            alerts.remove(alert)
            return 1
        }
        return 0
    }

    override fun deleteAlert(id: Int): Int {
        for(alert in alerts){
            if(alert.id == id){
                alerts.remove(alert)
                return 1
            }
        }
        return 0
    }

    override fun getAlerts(): Flow<List<AlertDTO>> = flowOf(alerts)

    override fun updateHomeLocation(homeLocation: HomeLocation): Long {
        TODO("Not yet implemented")
    }

    override fun getHomeLocation(): Flow<HomeLocation> {
        TODO("Not yet implemented")
    }

    override fun saveData(key: String, value: Any) {
        TODO("Not yet implemented")
    }

    override fun <T> fetchData(key: String, defaultValue: T): T {
        TODO("Not yet implemented")
    }

    override fun saveCurrentLocation(lat: Double, lon: Double) {
        TODO("Not yet implemented")
    }

    override fun getCurrentLocation(): Pair<Double, Double> {
        TODO("Not yet implemented")
    }

    override fun saveMapLocation(lat: Double, lon: Double) {
        TODO("Not yet implemented")
    }

    override fun getMapLocation(): Pair<Double, Double> {
        TODO("Not yet implemented")
    }
}