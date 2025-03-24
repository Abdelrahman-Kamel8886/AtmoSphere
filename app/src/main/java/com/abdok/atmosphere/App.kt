package com.abdok.atmosphere

import android.app.Application
import com.abdok.atmosphere.Data.Local.Room.LocalDataBase
import com.abdok.atmosphere.Data.Local.SharedPreference.SharedPreferencesImpl

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        LocalDataBase.initDataBase(this)
        SharedPreferencesImpl.initSharedPreferences(this)
    }
}