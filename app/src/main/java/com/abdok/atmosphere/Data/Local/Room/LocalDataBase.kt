package com.abdok.atmosphere.Data.Local.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.abdok.atmosphere.Data.Models.FavouriteLocation
import com.abdok.atmosphere.Utils.Constants
import com.abdok.atmosphere.Utils.TypeConverter.GsonTypeConverter

@Database(entities = [FavouriteLocation::class], version = 3 )
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