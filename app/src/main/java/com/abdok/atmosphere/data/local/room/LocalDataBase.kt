package com.abdok.atmosphere.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.abdok.atmosphere.data.models.AlertDTO
import com.abdok.atmosphere.data.models.FavouriteLocation
import com.abdok.atmosphere.data.models.HomeLocation
import com.abdok.atmosphere.utils.Constants
import com.abdok.atmosphere.utils.typeConverter.GsonTypeConverter

@Database(entities = [FavouriteLocation::class , HomeLocation::class , AlertDTO::class], version = 5,exportSchema = false)
@TypeConverters(GsonTypeConverter::class)
abstract class LocalDataBase : RoomDatabase() {

    abstract fun localDao(): LocalDao

    companion object{
        private var instance: LocalDataBase? = null

        fun initDataBase(context: Context){
                instance = Room.databaseBuilder(context, LocalDataBase::class.java, Constants.DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
        }

        fun getInstance() = instance!!
    }
}