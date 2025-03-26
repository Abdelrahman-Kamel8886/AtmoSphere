package com.abdok.atmosphere.View.Screens.Home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.abdok.atmosphere.Data.Models.CombinedWeatherData
import com.abdok.atmosphere.Data.Repository.Repository
import com.abdok.atmosphere.Data.Response
import com.abdok.atmosphere.Enums.Languages
import com.abdok.atmosphere.Enums.Locations
import com.abdok.atmosphere.Enums.Speeds
import com.abdok.atmosphere.Enums.Units
import com.abdok.atmosphere.Utils.Constants
import com.abdok.atmosphere.Utils.SharedModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {

    private var unit : String
    private var speed : String
    var location : Pair<Double , Double>

    init {
        unit = repository.fetchPreferenceData(Constants.TEMPERATURE_UNIT , Units.METRIC.value)
        SharedModel.currentDegree = Units.getDegreeByValue(unit)

        speed = repository.fetchPreferenceData(Constants.WIND_SPEED_UNIT , Speeds.METERS_PER_SECOND.degree)
        SharedModel.currentSpeed = Speeds.getDegree(speed)

        val loc = repository.getLocation()
        location = Pair(loc.first , loc.second)
    }

    private var mutableCombinedWeatherData = MutableStateFlow<Response<CombinedWeatherData>>(Response.Loading)
    val combinedWeatherData = mutableCombinedWeatherData.asStateFlow()

    private var mutableError :MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String> = mutableError

    fun getWeatherAndForecastLatLon(
        lat: Double = location.first,
        lon: Double = location.second,
        units: String = unit,
        lang: String = repository.fetchPreferenceData(Constants.LANGUAGE_CODE , Languages.ENGLISH.code)
    ){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val weatherDeferred = async { repository.getWeatherLatLon(lat, lon, units, lang) }
                val forecastDeferred = async { repository.getForecastLatLon(lat, lon, units, lang) }

                val weather = weatherDeferred.await()
                val forecast = forecastDeferred.await()

                mutableCombinedWeatherData.value = Response.Success(CombinedWeatherData(weather , forecast))

            }catch (exception : Exception){
                mutableCombinedWeatherData.value = Response.Error(exception.message.toString())
            }
        }
    }

    fun updateCurrentLocation(lat: Double, lon: Double){
        repository.saveLocation(Locations.Gps , lat, lon)
    }
    fun updateMapLocation(lat: Double , lon: Double){
        repository.saveLocation(Locations.Map , lat, lon)
    }


}

class HomeViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}
