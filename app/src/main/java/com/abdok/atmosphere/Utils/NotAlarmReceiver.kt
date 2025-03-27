package com.abdok.atmosphere.Utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.abdok.atmosphere.R

/*
lifecycleScope.launch(Dispatchers.IO) {
    setAlarm(this@MainActivity, 100000)
}
*/

class NotAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "Alarm Triggered!", Toast.LENGTH_SHORT).show()

        // Example: Show a notification when the alarm is triggered
        context?.let { createNotification(it) }
    }

    private fun createNotification(context: Context) {
        val channelId = "alarm_channel"
        val notificationId = 1

        // Create Notification Channel (required for Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Alarm Notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = context.getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.wind_icon) // Use your own icon
            .setContentTitle("Alarm")
            .setContentText("Your alarm is ringing!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val manager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        manager.notify(notificationId, builder.build())
    }
}

//@SuppressLint("ScheduleExactAlarm")
//fun setAlarm(context: Context, durationInMillis: Long) {
//    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//    val intent = Intent(context, AlarmReceiver::class.java)
//    val pendingIntent = PendingIntent.getBroadcast(
//        context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//    )
//
//    val triggerTime = System.currentTimeMillis() + durationInMillis
//
//    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
//
//    //Toast.makeText(context, "Alarm set for ${durationInMillis / 1000} seconds", Toast.LENGTH_SHORT).show()
//}
//
