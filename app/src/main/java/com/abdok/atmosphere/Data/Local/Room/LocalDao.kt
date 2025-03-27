package com.abdok.atmosphere.Data.Local.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.abdok.atmosphere.Data.Models.FavouriteLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalDao {

    @Query("SELECT * FROM favourite_table")
    fun getAllFavouriteLocations(): Flow<List<FavouriteLocation>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavouriteLocation(favouriteLocation: FavouriteLocation) : Long

    @Delete
    fun deleteFavouriteLocation(favouriteLocation: FavouriteLocation) : Int

    @Update
    fun updateFavouriteLocation(favouriteLocation: FavouriteLocation) : Int
}