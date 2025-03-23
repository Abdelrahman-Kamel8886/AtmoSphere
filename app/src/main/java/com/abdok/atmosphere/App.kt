package com.abdok.atmosphere

import android.app.Application
import com.abdok.atmosphere.Data.Local.Room.LocalDataBase

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        LocalDataBase.initDataBase(this)
    }
}