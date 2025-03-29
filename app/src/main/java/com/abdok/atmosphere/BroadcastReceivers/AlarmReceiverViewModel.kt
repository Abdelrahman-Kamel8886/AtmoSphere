package com.abdok.atmosphere.BroadcastReceivers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.abdok.atmosphere.Data.Models.CombinedWeatherData
import com.abdok.atmosphere.Data.Models.WeatherResponse
import com.abdok.atmosphere.Data.Repository
import com.abdok.atmosphere.Data.Response
import com.abdok.atmosphere.Enums.Units
import com.abdok.atmosphere.Utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.Locale

class AlarmReceiverViewModel(private val repository: Repository) : ViewModel(){

    private var mutableCombinedWeatherData = MutableStateFlow<Response<WeatherResponse>>(Response.Loading)
    val combinedWeatherData = mutableCombinedWeatherData.asStateFlow()

    fun getWeatherForAlarm(
        lat: Double = Constants.DEFAULT_LAT,
        lon: Double = Constants.DEFAULT_LON,
        units: String  = Units.METRIC.value,
        lang: String = Locale.getDefault().language
    ){

        viewModelScope.launch(Dispatchers.IO) {
            repository.getWeatherLatLon(lat, lon, units, lang)
                .catch {
                    mutableCombinedWeatherData.value = Response.Error(it.message.toString())
                }.collect{
                    mutableCombinedWeatherData.value = Response.Success(it)
                }
        }


    }

}

class AlarmReceiverViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlarmReceiverViewModel(repository) as T
    }
}