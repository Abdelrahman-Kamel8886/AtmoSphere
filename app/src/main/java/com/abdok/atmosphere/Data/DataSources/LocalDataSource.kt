package com.abdok.atmosphere.Data.DataSources

import com.abdok.atmosphere.Data.Local.Room.LocalDao
import com.abdok.atmosphere.Data.Models.FavouriteLocation

class LocalDataSource private constructor(val dao: LocalDao) {

    suspend fun insertFavoriteLocation(favoriteLocation: FavouriteLocation) = dao.insertFavouriteLocation(favoriteLocation)
    suspend fun deleteFavoriteLocation(favoriteLocation: FavouriteLocation) = dao.deleteFavouriteLocation(favoriteLocation)
    suspend fun getFavoriteLocations() = dao.getAllFavouriteLocations()



    companion object {
        private var instance: LocalDataSource? = null
        fun getInstance(dao: LocalDao): LocalDataSource {
            if (instance == null) {
                instance = LocalDataSource(dao)
            }
            return instance!!
        }
    }

}