package com.abdok.atmosphere.utils.typeConverter

import androidx.room.TypeConverter
import com.abdok.atmosphere.data.models.CombinedWeatherData
import com.abdok.atmosphere.data.models.FavouriteLocation
import com.abdok.atmosphere.enums.Alert
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class GsonTypeConverter {

    val gson = Gson()

    @TypeConverter
    fun fromLatLng(latLng: LatLng?): String? {
        return latLng?.let { "${it.latitude},${it.longitude}" }
    }

    @TypeConverter
    fun toLatLng(value: String?): LatLng? {
        return value?.split(",")?.let {
            LatLng(it[0].toDouble(), it[1].toDouble())
        }
    }

    @TypeConverter
    fun fromCombinedWeatherData(combinedWeatherData: CombinedWeatherData?): String? {
        return gson.toJson(combinedWeatherData)
    }

    @TypeConverter
    fun toCombinedWeatherData(value: String?): CombinedWeatherData? {
        return value?.let {
            gson.fromJson(it, object : TypeToken<CombinedWeatherData>() {}.type)
        }
    }

    @TypeConverter
    fun fromAlert(alert: Alert): String {
        return alert.name
    }

    @TypeConverter
    fun toAlert(value: String): Alert {
        return Alert.valueOf(value)
    }

    fun toJson(location: FavouriteLocation): String {
        return gson.toJson(location)
    }

    fun fromJson(json: String): FavouriteLocation {
        return gson.fromJson(json, FavouriteLocation::class.java)
    }
}