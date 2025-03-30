package com.abdok.atmosphere.enums

import java.util.Locale

enum class Speeds(val degree: String, val arDegree: String) {
    METERS_PER_SECOND("m/s", "م/ث"),
    MILES_PER_HOUR("mph", "ميل/س");

    companion object {
        fun getDegree(degree: String): String {
            val language = Locale.getDefault().language

            if (language == Languages.ARABIC.code) {
                return  Speeds.values().find { it.degree == degree }?.arDegree
                    ?: METERS_PER_SECOND.arDegree
            }
            return degree
        }

        fun getEnglishDegree(degree: String): String {
            val language = Locale.getDefault().language

            if (language == Languages.ARABIC.code) {
                return Speeds.values().find { it.arDegree == degree }?.degree
                    ?: METERS_PER_SECOND.degree
            }
            return degree

        }

    }
}