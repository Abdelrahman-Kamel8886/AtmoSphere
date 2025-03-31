package com.abdok.atmosphere.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.abdok.atmosphere.data.RepositoryImpl
import com.abdok.atmosphere.data.local.LocalDataSourceImpl
import com.abdok.atmosphere.data.local.room.LocalDataBase
import com.abdok.atmosphere.data.local.sharedPreference.SharedPreferencesImpl
import com.abdok.atmosphere.data.models.WeatherResponse
import com.abdok.atmosphere.data.remote.RemoteDataSourceImpl
import com.abdok.atmosphere.data.remote.retrofit.RetroConnection
import com.abdok.atmosphere.enums.Units
import com.abdok.atmosphere.utils.Constants
import com.abdok.atmosphere.utils.notification.NotificationHelper
import com.abdok.atmosphere.utils.extension.getWeatherNotification
import com.abdok.atmosphere.utils.extension.translateWeatherCondition
import com.abdok.atmosphere.utils.viewHelpers.IconsMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.Locale

class NotificationWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params)  {

    override suspend fun doWork(): Result {

        val repository = RepositoryImpl.getInstance(
            RemoteDataSourceImpl.getInstance(RetroConnection.retroServices),
            LocalDataSourceImpl.getInstance(
                LocalDataBase.getInstance().localDao(), SharedPreferencesImpl.getInstance()
            )
        )

        val id = inputData.getInt(Constants.ALARM_ID, -1)
        Log.i("TAG", "onReceive: $id")

        removeAlarm(id, repository)
        getData(repository)


        return Result.success()
    }

    private fun removeAlarm(id: Int, repository: RepositoryImpl) {
        val result = repository.deleteAlert(id)
        Log.i("TAG", "removeAlarm: $result")
    }

    private fun getData(repository: RepositoryImpl) {
        GlobalScope.launch(Dispatchers.IO) {
            repository
                .getWeatherLatLon(
                    Constants.DEFAULT_LAT,
                    Constants.DEFAULT_LON,
                    Units.METRIC.value, Locale.getDefault().language
                )
                .catch {
                    Log.i("TAG", "onReceive: ")
                }
                .collect {
                    if (it != null) {
                        showNotification(it)
                    }
                }

        }
    }

    private fun showNotification(weatherResponse: WeatherResponse) {
        val condition = weatherResponse.weather[0].icon

        val icon = IconsMapper.getIcon(condition)
        val title = weatherResponse.weather[0].description.translateWeatherCondition()
        val message = condition.getWeatherNotification()

        NotificationHelper
            .showNotification(applicationContext, title,message, icon)
    }

}