package com.abdok.atmosphere.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson

@Entity(tableName = "favourite_table")
data class FavouriteLocation(
    @PrimaryKey
    val cityName: String,
    val countryName: String,
    val location: LatLng,
    val combinedWeatherData: CombinedWeatherData
){
    companion object {
        private val gson = Gson()
        fun fromJson(json: String): FavouriteLocation {
            return gson.fromJson(json, FavouriteLocation::class.java)
        }
        fun FavouriteLocation.toJson(): String {
            return gson.toJson(this)
        }
    }
}
