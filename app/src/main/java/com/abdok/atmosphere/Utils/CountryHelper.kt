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
    fun getFlagEmoji(area: String?): String {
        val countryCode: String =area?: return ""
        val flag = StringBuilder()
        for (c in countryCode.uppercase(Locale.getDefault()).toCharArray()) {
            flag.appendCodePoint(127397 + c.code)
        }
        return flag.toString()
    }
}