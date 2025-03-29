package com.abdok.atmosphere.BroadcastReceivers

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.abdok.atmosphere.Data.Local.LocalDataSource
import com.abdok.atmosphere.Data.Local.Room.LocalDataBase
import com.abdok.atmosphere.Data.Local.SharedPreference.SharedPreferencesImpl
import com.abdok.atmosphere.Data.Remote.RemoteDataSource
import com.abdok.atmosphere.Data.Remote.Retrofit.RetroConnection
import com.abdok.atmosphere.Data.Repository
import com.abdok.atmosphere.Enums.Units
import com.abdok.atmosphere.R
import com.abdok.atmosphere.Utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Locale

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        private const val CHANNEL_ID = "ALARM_CHANNEL"
        private var mediaPlayer: MediaPlayer? = null

        fun stopAlarm(context: Context) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null

            val manager = NotificationManagerCompat.from(context)
            manager.cancel(1)
        }
    }


    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        createNotificationChannel(context)

        GlobalScope.launch(Dispatchers.IO){
           val data =  RetroConnection.retroServices
                .getWeatherLatLon(
                    Constants.DEFAULT_LAT ,
                    Constants.DEFAULT_LON ,
                    Units.METRIC.value
                    , Locale.getDefault().language)

            data?.let {
                Log.i("TAG", "onReceive: ${data.name} ${data.weather[0].description}")
            }

        }

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.alarm1)
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
        }

        val stopIntent = Intent(context, StopAlarmReceiver::class.java)
        val stopPendingIntent = PendingIntent.getBroadcast(
            context, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.few_clouds_day) // Add your alarm icon in res/drawable
            .setContentTitle("Alarm")
            .setContentText("Tap STOP to turn off the alarm")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .addAction(R.drawable.few_clouds_night, "STOP", stopPendingIntent)
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(1, notification)
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Alarm Channel",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for alarm notifications"
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }
}


