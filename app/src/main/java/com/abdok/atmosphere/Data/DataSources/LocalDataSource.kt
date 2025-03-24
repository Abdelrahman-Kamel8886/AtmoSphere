package com.abdok.atmosphere.Data.DataSources

import com.abdok.atmosphere.Data.Local.Room.LocalDao
import com.abdok.atmosphere.Data.Local.SharedPreference.ISharedPreferences
import com.abdok.atmosphere.Data.Models.FavouriteLocation

class LocalDataSource private constructor(val dao: LocalDao , val sharedPreferences: ISharedPreferences) {

    //Room
    fun insertFavoriteLocation(favoriteLocation: FavouriteLocation) = dao.insertFavouriteLocation(favoriteLocation)
    fun deleteFavoriteLocation(favoriteLocation: FavouriteLocation) = dao.deleteFavouriteLocation(favoriteLocation)
    fun getFavoriteLocations() = dao.getAllFavouriteLocations()

    //SharedPreferences
    fun saveData(key: String, value: Any) = sharedPreferences.saveData(key, value)
    fun <T> fetchData(key: String, defaultValue: T) = sharedPreferences.fetchData(key, defaultValue)

    companion object {
        private var instance: LocalDataSource? = null
        fun getInstance(dao: LocalDao , sharedPreferences: ISharedPreferences): LocalDataSource {
            if (instance == null) {
                instance = LocalDataSource(dao , sharedPreferences)
            }
            return instance!!
        }
    }

}