package com.abdok.atmosphere.Data.Models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

@Entity(tableName = "favourite_table")
data class FavouriteLocation(
    @PrimaryKey
    val name: String,
    val location: LatLng,
    val combinedWeatherData: CombinedWeatherData
)
