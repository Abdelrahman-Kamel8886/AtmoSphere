package com.abdok.atmosphere.Utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.util.Log
import com.abdok.atmosphere.Data.Models.ForecastResponse
import com.abdok.atmosphere.BroadcastReceivers.AlarmReceiver
import com.abdok.atmosphere.FloatingWindowService
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun ForecastResponse.getDaysForecast(): Map<Int, List<ForecastResponse.Item0>> {
    val forecastMap = mutableMapOf<Int, MutableList<ForecastResponse.Item0>>()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    for (item in list) {
        val dateKey = dateFormat.format(Date(item.dt * 1000L)).replace("-", "").toInt()
        if (forecastMap[dateKey] == null) {
            forecastMap[dateKey] = mutableListOf()
        }
        forecastMap[dateKey]?.add(item)
    }
    return forecastMap.mapValues { it.value.take(8) }
}


@SuppressLint("ScheduleExactAlarm")
fun Context.setAlarm(seconds: Int) {
    val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(this, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    val triggerTime = SystemClock.elapsedRealtime() + (seconds * 1000)
    alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent)
}

//fun Context.startOverlayService() {
//    Log.d("Overlay", "Starting overlay service")
//    val intent = Intent(this, FloatingWindowService::class.java)
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        Log.d("Overlay", "Starting overlay service1111111111")
//
//        startForegroundService(intent)
//    } else {
//        Log.d("Overlay", "Starting overlay service22222222222222222")
//
//        startService(intent)
//    }
//}

