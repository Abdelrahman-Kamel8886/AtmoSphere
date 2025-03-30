package com.abdok.atmosphere

import android.app.Application
import com.abdok.atmosphere.data.local.room.LocalDataBase
import com.abdok.atmosphere.data.local.sharedPreference.SharedPreferencesImpl

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        LocalDataBase.initDataBase(this)
        SharedPreferencesImpl.initSharedPreferences(this)
    }
}