package com.abdok.atmosphere.enums

import java.util.Locale

enum class Locations (val value: String , val arValue: String , val esValue: String){
    Gps("Gps" , " نظام تحديد المواقع ","GPS"),
    Map("Map","الخرائط","Mapa");

    companion object{
        fun getValue(value: String): String {
            val language = Locale.getDefault().language
            return when(language){
                Languages.ARABIC.code -> {
                    Locations.values().find { it.value == value }?.arValue ?: Gps.arValue
                }
                Languages.SPANISH.code -> {
                    Locations.values().find { it.value == value }?.esValue ?: Gps.esValue
                }

                else -> {
                    Locations.values().find { it.value == value }?.value ?: Gps.value
                }

            }
        }

        fun getEnglishValue(value: String): String {
            val language = Locale.getDefault().language

            return when (language) {
                Languages.ARABIC.code -> {
                    Locations.values().find { it.arValue == value }?.value
                        ?: Gps.value
                }
                Languages.SPANISH.code -> {
                    Locations.values().find { it.esValue == value }?.value
                        ?: Gps.value
                }
                else -> {
                    value
                }
            }
        }
    }
}

