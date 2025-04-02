package com.abdok.atmosphere.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.abdok.atmosphere.data.local.LocalDataSourceImpl
import com.abdok.atmosphere.data.local.room.LocalDataBase
import com.abdok.atmosphere.data.local.sharedPreference.SharedPreferencesImpl
import com.abdok.atmosphere.data.models.AlertDTO
import com.abdok.atmosphere.data.remote.RemoteDataSourceImpl
import com.abdok.atmosphere.data.remote.retrofit.RetroConnection
import com.abdok.atmosphere.data.repository.RepositoryImpl
import com.abdok.atmosphere.utils.Constants
import com.abdok.atmosphere.utils.extension.addMinutes
import com.abdok.atmosphere.utils.extension.convertArabicToEnglish
import com.abdok.atmosphere.utils.extension.durationFromNowInSeconds
import com.abdok.atmosphere.utils.extension.setAlarm
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StopAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {

        val isSnooze = intent?.getBooleanExtra("SNOOZE", false) ?: false

        if (isSnooze) {
            val alert = Gson().fromJson(intent?.getStringExtra(Constants.ALARM_ID) ?: return, AlertDTO::class.java)
            snoozeAlarm(context, alert)
        }
        AlarmReceiver.stopAlarm(context)
    }



    fun snoozeAlarm(context: Context, alert: AlertDTO) {
        val repository = RepositoryImpl.getInstance(
            RemoteDataSourceImpl.getInstance(RetroConnection.retroServices),
            LocalDataSourceImpl.getInstance(
                LocalDataBase.getInstance().localDao(), SharedPreferencesImpl.getInstance()
            )
        )

        val id = System.currentTimeMillis().toInt()
        val startDuration = alert.startDuration.addMinutes(5)
        val endDuration = alert.endDuration.addMinutes(5)
        val selectedOption = alert.selectedOption

        val newAlert = AlertDTO(
            id = id,
            startDuration = startDuration.convertArabicToEnglish(),
            endDuration = endDuration.convertArabicToEnglish(),
            selectedOption = selectedOption
        )

        GlobalScope.launch(Dispatchers.IO) {
            repository.insertAlert(newAlert)
            val startSeconds = startDuration.durationFromNowInSeconds()
            val endSeconds = endDuration.durationFromNowInSeconds()
            context.setAlarm(startSeconds, endSeconds, newAlert)
        }

    }

}
