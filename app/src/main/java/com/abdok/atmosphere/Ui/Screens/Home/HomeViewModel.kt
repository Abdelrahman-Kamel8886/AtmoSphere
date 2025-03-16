package com.abdok.atmosphere.Ui.Screens.Home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.abdok.atmosphere.Data.Models.CombinedWeatherData
import com.abdok.atmosphere.Repository
import com.abdok.atmosphere.Utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {

    /*private var mutableWeatherData: MutableLiveData<WeatherResponse> = MutableLiveData()
    val weatherData: LiveData<WeatherResponse> = mutableWeatherData

    private var mutableForecastData: MutableLiveData<ForecastResponse> = MutableLiveData()
    val forecastData: LiveData<ForecastResponse> = mutableForecastData*/

    private var mutableCombinedWeatherData: MutableLiveData<CombinedWeatherData> = MutableLiveData()
    val combinedWeatherData: LiveData<CombinedWeatherData> = mutableCombinedWeatherData

    private var mutableError :MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String> = mutableError

    fun getWeatherAndForecastLatLon(
        lat: Double,
        lon: Double,
        units: String = Constants.unit,
        lang: String = Constants.DEFAULT_LANG
    ){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val weatherDeferred = async { repository.getWeatherLatLon(lat, lon, units, lang) }
                val forecastDeferred = async { repository.getForecastLatLon(lat, lon, units, lang) }

                val weather = weatherDeferred.await()
                val forecast = forecastDeferred.await()

                mutableCombinedWeatherData.postValue(CombinedWeatherData(weather , forecast))

            }catch (exception : Exception){
                mutableError.postValue(exception.message)
            }
        }
    }




   /* fun getWeatherLatLon(
        lat: Double,
        lon: Double,
        units: String = Units.METRIC.value,
        lang: String = Consts.DEFAULT_LANG
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getWeatherLatLon(lat , lon , units , lang)
                mutableWeatherData.postValue(response)
            }catch (exception : Exception){
                mutableError.postValue(exception.message)
            }
        }
    }

    fun getForecastLatLon(
        lat: Double,
        lon: Double,
        units: String = Units.METRIC.value,
        lang: String = Consts.DEFAULT_LANG
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getForecastLatLon(lat , lon , units , lang)
                mutableForecastData.postValue(response)
            }catch (exception : Exception){
                mutableError.postValue(exception.message)
        }
    }*/
}

class HomeViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}
