package com.abdok.atmosphere.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.abdok.atmosphere.data.local.LocalDataSourceImpl
import com.abdok.atmosphere.data.local.room.LocalDataBase
import com.abdok.atmosphere.data.local.sharedPreference.SharedPreferencesImpl
import com.abdok.atmosphere.data.remote.RemoteDataSourceImpl
import com.abdok.atmosphere.data.remote.retrofit.RetroConnection
import com.abdok.atmosphere.data.repository.RepositoryImpl
import com.abdok.atmosphere.utils.Constants

class LanguageChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_LOCALE_CHANGED) {

            val newLanguage = getCurrentLanguage(context)
            Log.i("TAG", "onReceive: $newLanguage")

            val repository = RepositoryImpl.getInstance(
                RemoteDataSourceImpl.getInstance(RetroConnection.retroServices),
                LocalDataSourceImpl.getInstance(
                    LocalDataBase.getInstance().localDao(), SharedPreferencesImpl.getInstance()
                )
            )
            repository.savePreferenceData(Constants.LANGUAGE_CODE , newLanguage)

        }
    }

    private fun getCurrentLanguage(context: Context): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0].language
        } else {
            context.resources.configuration.locale.language
        }
    }
}