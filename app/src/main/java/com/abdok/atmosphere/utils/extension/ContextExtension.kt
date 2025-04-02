package com.abdok.atmosphere.utils.extension

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.abdok.atmosphere.receivers.AlarmReceiver
import com.abdok.atmosphere.receivers.StopAlarmReceiver
import com.abdok.atmosphere.utils.Constants
import com.abdok.atmosphere.workers.NotificationWorker
import com.google.android.gms.location.LocationServices
import java.util.concurrent.TimeUnit

@SuppressLint("MissingPermission")
fun Context.getGpsLocation(onLocationReceived: (location: Location) -> Unit){
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

    fusedLocationProviderClient.lastLocation
        .addOnSuccessListener {
            Log.i("TAG", "getGpsLocation: ")
            if (it != null) {
                Log.i("TAG", "getGpsLocation: 111 ${it.latitude} ${it.longitude}")
                onLocationReceived(it)
            }
        }.addOnFailureListener {
            Log.i("TAG", "getGpsLocation: 222 ${it.message}")
            it.printStackTrace()
        }
}

fun Context.isNetworkConnected(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    } else {
        val networkInfo = connectivityManager.activeNetworkInfo
        networkInfo != null && networkInfo.isConnected
    }
}

//@SuppressLint("ScheduleExactAlarm")
//fun Context.setAlarm(seconds: Int , id: Int) {
//    val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//    val intent = Intent(this, AlarmReceiver::class.java)
//        .putExtra(Constants.ALARM_ID,id)
//    val pendingIntent = PendingIntent.getBroadcast(
//        this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
//    val triggerTime = SystemClock.elapsedRealtime() + (seconds * 1000)
//    alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent)
//
//}

@SuppressLint("ScheduleExactAlarm")
fun Context.setAlarm(seconds: Int, id: Int, duration: Int) {
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // Set the alarm
    val alarmIntent = Intent(this, AlarmReceiver::class.java)
        .putExtra(Constants.ALARM_ID, id)

    val alarmPendingIntent = PendingIntent.getBroadcast(
        this, id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

    val triggerTime = SystemClock.elapsedRealtime() + (seconds * 1000)
    alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, alarmPendingIntent)

    // Schedule stopping the alarm after the duration
    val stopIntent = Intent(this, StopAlarmReceiver::class.java)
    val stopPendingIntent = PendingIntent.getBroadcast(
        this, id, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val stopTime = triggerTime + (duration * 1000)
    alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, stopTime, stopPendingIntent)
}


fun Context.cancelAlarm(id: Int) {
    val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(this, AlarmReceiver::class.java)
        .putExtra(Constants.ALARM_ID, id)

    val pendingIntent = PendingIntent.getBroadcast(
        this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    alarmManager.cancel(pendingIntent)
}

fun Context.scheduleNotification(id: Int, delayInSeconds: Int) {

    val inputData = Data.Builder()
        .putInt(Constants.ALARM_ID, id)
        .build()

    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()


    val workRequest: WorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInitialDelay(delayInSeconds.toLong(), TimeUnit.SECONDS)
        .setInputData(inputData)
        .setConstraints(constraints)
        .addTag("notification_$id")
        .build()

    WorkManager.getInstance(this).enqueue(workRequest)
}

fun Context.cancelNotification(id: Int) {
    WorkManager.getInstance(this).cancelAllWorkByTag("notification_$id")
}

