package com.abdok.atmosphere.Enums

enum class Languages(val value: String, val code: String) {

    ENGLISH("English", "en"),
    ARABIC("العربية", "ar"),
    SPANISH("Español", "es");


    companion object{
        fun getCodeByValue(value : String): String {
            return values().find { it.value == value }?.code ?: ENGLISH.code
        }
        fun getValueByCode(code : String): String {
            return values().find { it.code == code }?.value ?: ENGLISH.value
        }
    }
}