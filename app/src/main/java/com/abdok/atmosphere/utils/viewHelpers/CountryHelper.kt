package com.abdok.atmosphere.utils.viewHelpers

import android.content.Context
import android.location.Geocoder
import java.io.IOException
import java.util.Locale

object CountryHelper {
    fun getCountryNameFromCode(code: String): String? {
        return try {
            Locale("", code).displayCountry.takeIf { it.isNotBlank() }
        } catch (e: Exception) {
            null
        }
    }
    fun getFlagEmoji(area: String?): String {
        val countryCode: String =area?: return ""
        val flag = StringBuilder()
        for (c in countryCode.uppercase(Locale.getDefault()).toCharArray()) {
            flag.appendCodePoint(127397 + c.code)
        }
        return flag.toString()
    }

    fun getLocalizedCityName(context: Context, cityName: String): String {
        val locale = Locale.getDefault()
        val geocoder = Geocoder(context, locale)
        return try {
            val addresses = geocoder.getFromLocationName(cityName, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                (address.locality)
            } else {
                cityName
            }
        } catch (e: IOException) {
            e.printStackTrace()
            cityName
        }
    }
}