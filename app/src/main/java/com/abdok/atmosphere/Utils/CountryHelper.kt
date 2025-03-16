package com.abdok.atmosphere.Utils

import java.util.Locale

object CountryHelper {
    fun getCountryNameFromCode(code: String): String? {
        return try {
            Locale("", code).displayCountry.takeIf { it.isNotBlank() }
        } catch (e: Exception) {
            null
        }
    }
}