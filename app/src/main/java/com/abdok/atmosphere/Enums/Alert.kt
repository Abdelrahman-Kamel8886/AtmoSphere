package com.abdok.atmosphere.Enums

import java.util.Locale

enum class Alert(val value: String , val arValue : String , val esValue : String) {
    ALARM("Alarm" , "منبه" , "Alarma"),
    NOTIFICATION("Notification" , "الإشعار" , "Notificación");

    fun getLocalizedValue() : String {
        val lang = Locale.getDefault().language
        return when(lang){
            "ar" -> arValue
            "es" -> esValue
            else -> value
        }
    }

    companion object {
        val lang: String = Locale.getDefault().language

        fun getAbsoluteValue(value: String): Alert {
            return when(lang){
                "ar" -> values().find { it.arValue == value } ?: ALARM
                "es" -> values().find { it.esValue == value } ?: ALARM
                else -> values().find { it.value == value } ?: ALARM
            }
        }
    }




}