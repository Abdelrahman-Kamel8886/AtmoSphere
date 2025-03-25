package com.abdok.atmosphere.Enums

import com.abdok.atmosphere.Enums.Languages.ENGLISH
import java.util.Locale

enum class Units(val value: String , val degree:String , val arDegree:String)  {

    STANDARD("standard","°K","°ك"),
    METRIC("metric","°C","°س"),
    IMPERIAL("imperial","°F","°ف");

    companion object{

        fun getValueByDegree(degree : String): String {
            val language = Locale.getDefault().language

            if (language == Languages.ARABIC.code) {
                return Units.values().find { it.arDegree == degree }?.value ?: METRIC.value
            }
            else{
                return Units.values().find { it.degree == degree }?.value ?: METRIC.degree
            }
        }

        fun getDegreeByValue(value: String): String {
            val language = Locale.getDefault().language

            if (language == Languages.ARABIC.code) {
                return Units.values().find { it.value == value }?.arDegree ?: METRIC.arDegree
            }
            else{
                return Units.values().find { it.value == value }?.degree ?: METRIC.degree
            }

        }
    }
}