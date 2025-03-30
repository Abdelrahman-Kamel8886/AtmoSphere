package com.abdok.atmosphere.data

import com.abdok.atmosphere.data.models.AlertDTO
import com.abdok.atmosphere.data.models.CityLocationResponseItem
import com.abdok.atmosphere.data.models.FavouriteLocation
import com.abdok.atmosphere.data.models.ForecastResponse
import com.abdok.atmosphere.data.models.HomeLocation
import com.abdok.atmosphere.data.models.WeatherResponse
import com.abdok.atmosphere.enums.Locations
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun getWeatherLatLon(lat: Double, lon: Double, units: String, lang: String) : Flow<WeatherResponse>
    suspend fun getForecastLatLon(lat: Double, lon: Double, units: String, lang: String) : Flow<ForecastResponse>
    suspend fun getCityLocation(cityName: String) : Flow<List<CityLocationResponseItem>>
    suspend fun getCityName(lat: Double, lon: Double) : Flow<List<CityLocationResponseItem>>


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

    fun savePreferenceData(key: String, value: Any)
    fun <T> fetchPreferenceData(key: String, defaultValue: T): T

    fun getLocation():Pair<Double , Double>
    fun saveLocation(locationType : Locations, lat: Double, lon: Double)
}