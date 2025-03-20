package com.abdok.atmosphere.Data.Local.SharedPreference

interface ISharedPreferences {
    fun <T> saveData(key: String, value: T)
    fun <T> fetchData(key: String, defaultValue: T): T
}