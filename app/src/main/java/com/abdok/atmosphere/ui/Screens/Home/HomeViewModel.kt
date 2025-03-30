package com.abdok.atmosphere.ui.Screens.Home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.abdok.atmosphere.data.models.CombinedWeatherData
import com.abdok.atmosphere.data.models.HomeLocation
import com.abdok.atmosphere.data.Repository
import com.abdok.atmosphere.data.Response
import com.abdok.atmosphere.enums.Languages
import com.abdok.atmosphere.enums.Locations
import com.abdok.atmosphere.enums.Speeds
import com.abdok.atmosphere.enums.Units
import com.abdok.atmosphere.utils.Constants
import com.abdok.atmosphere.utils.SharedModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
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

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        getHomeLocation()
    }

    fun getWeatherAndForecastLatLon(
        lat: Double = location.first,
        lon: Double = location.second,
        units: String = unit,
        lang: String = repository.fetchPreferenceData(Constants.LANGUAGE_CODE , Languages.ENGLISH.code)
    ){
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            mutableCombinedWeatherData.value = Response.Loading
            try {

                val weatherDeferred = async { repository.getWeatherLatLon(lat, lon, units, lang) }
                val forecastDeferred = async { repository.getForecastLatLon(lat, lon, units, lang) }

                val weather = weatherDeferred.await().firstOrNull()
                val forecast = forecastDeferred.await().firstOrNull()

                if (weather != null && forecast != null) {
                    val data = CombinedWeatherData(weather, forecast)
                    mutableCombinedWeatherData.value = Response.Success(data)
                    updateHomeLocation(data)
                }
                else{
                    getHomeLocation()
                }


            } catch (exception: Exception) {
                getHomeLocation()

            }
        }
    }

    fun updateCurrentLocation(lat: Double, lon: Double){
        repository.saveLocation(Locations.Gps , lat, lon)
    }

    private fun getHomeLocation(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getHomeLocation()
                .catch {
                    mutableCombinedWeatherData.value = Response.Error(it.message.toString())
                }
                .collect{
                    if (it != null){
                        mutableCombinedWeatherData.value = Response.Success(it.combinedWeatherData)
                    }
                    else{
                        mutableCombinedWeatherData.value = Response.Error("No Data Found")
                    }
            }
        }
    }

    private fun updateHomeLocation(combinedWeatherData: CombinedWeatherData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateHomeLocation(HomeLocation(combinedWeatherData = combinedWeatherData))
        }
    }


}

class HomeViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}
