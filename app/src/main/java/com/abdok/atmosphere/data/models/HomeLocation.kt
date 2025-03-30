package com.abdok.atmosphere.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "home_location_table")
data class HomeLocation(
    @PrimaryKey
    val id: Int = 0,
    val combinedWeatherData: CombinedWeatherData
)