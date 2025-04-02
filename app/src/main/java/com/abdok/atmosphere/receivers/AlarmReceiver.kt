package com.abdok.atmosphere.receivers

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
import com.abdok.atmosphere.data.local.LocalDataSourceImpl
import com.abdok.atmosphere.data.local.room.LocalDataBase
import com.abdok.atmosphere.data.local.sharedPreference.SharedPreferencesImpl
import com.abdok.atmosphere.data.models.WeatherResponse
import com.abdok.atmosphere.data.remote.RemoteDataSourceImpl
import com.abdok.atmosphere.data.remote.retrofit.RetroConnection
import com.abdok.atmosphere.data.repository.RepositoryImpl
import com.abdok.atmosphere.enums.Units
import com.abdok.atmosphere.R
import com.abdok.atmosphere.screens.MainActivity
import com.abdok.atmosphere.utils.Constants
import com.abdok.atmosphere.utils.viewHelpers.IconsMapper
import com.abdok.atmosphere.utils.extension.getWeatherNotification
import com.abdok.atmosphere.utils.extension.translateWeatherCondition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
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

    override fun onReceive(context: Context, intent: Intent) {
        createNotificationChannel(context)

        val id = intent.getIntExtra(Constants.ALARM_ID, -1)
        Log.i("TAG", "onReceive: $id")

        val repository = RepositoryImpl.getInstance(
            RemoteDataSourceImpl.getInstance(RetroConnection.retroServices),
            LocalDataSourceImpl.getInstance(
                LocalDataBase.getInstance().localDao(), SharedPreferencesImpl.getInstance()
            )
        )
        removeAlarm(id, repository)
        getData(context, repository)
    }

    private fun removeAlarm(id: Int, repository: RepositoryImpl) {
        val result = repository.deleteAlert(id)
        Log.i("TAG", "removeAlarm: $result")
    }

    private fun getData(context: Context, repository: RepositoryImpl) {
        GlobalScope.launch(Dispatchers.IO) {
            repository
                .getWeatherLatLon(
                    Constants.DEFAULT_LAT,
                    Constants.DEFAULT_LON,
                    Units.METRIC.value, Locale.getDefault().language
                )
                .catch {
                    showNotification(context, null)
                }
                .collect {
                    if (it != null) {
                        showNotification(context, it)
                    }
                }

        }
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(context: Context, weatherResponse: WeatherResponse?) {
        
        var icon = R.drawable.few_clouds_night
        var title = context.getString(R.string.weather_details)
        var message = context.getString(R.string.check_your_internet_connection)
        
        weatherResponse?.let {
            val condition = weatherResponse.weather[0].icon
            icon = IconsMapper.getIcon(condition)
            title = weatherResponse.weather[0].description.translateWeatherCondition()
            message = condition.getWeatherNotification()
        }

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.alarm1)
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
        }

        val myIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val activityPendingIntent = PendingIntent.getActivity(
            context, 1,
            myIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(context, StopAlarmReceiver::class.java)
        val stopPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentIntent(activityPendingIntent)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("$message \n${context.getString(R.string.tap_stop_to_turn_off_the_alarm)}")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .addAction(R.drawable.few_clouds_night, context.getString(R.string.stop), stopPendingIntent)
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


