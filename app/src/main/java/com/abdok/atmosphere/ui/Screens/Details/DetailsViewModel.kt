package com.abdok.atmosphere.ui.Screens.Details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.abdok.atmosphere.Data.Models.CombinedWeatherData
import com.abdok.atmosphere.Data.Models.FavouriteLocation
import com.abdok.atmosphere.Data.Repository
import com.abdok.atmosphere.Data.Response
import com.abdok.atmosphere.Enums.Languages
import com.abdok.atmosphere.Enums.Speeds
import com.abdok.atmosphere.Enums.Units
import com.abdok.atmosphere.Utils.Constants
import com.abdok.atmosphere.Utils.SharedModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class DetailsViewModel(val repository: Repository) : ViewModel() {

    private var unit: String
    private var speed: String
    var location: Pair<Double, Double>

    init {
        unit = repository.fetchPreferenceData(Constants.TEMPERATURE_UNIT, Units.METRIC.value)
        SharedModel.currentDegree = Units.getDegreeByValue(unit)

        speed = repository.fetchPreferenceData(
            Constants.WIND_SPEED_UNIT,
            Speeds.METERS_PER_SECOND.degree
        )
        SharedModel.currentSpeed = Speeds.getDegree(speed)

        val loc = repository.getLocation()
        location = Pair(loc.first, loc.second)
    }

    private var mutableCombinedWeatherData = MutableStateFlow<Response<CombinedWeatherData>>(Response.Loading)
    val combinedWeatherData = mutableCombinedWeatherData.asStateFlow()

    private var mutableError: MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String> = mutableError

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        mutableCombinedWeatherData.value = Response.Error(throwable.message ?: "Unknown error occurred")
    }

    fun getWeatherAndForecast(
        favouriteLocation: FavouriteLocation,
        units: String = unit,
        lang: String = repository.fetchPreferenceData(Constants.LANGUAGE_CODE, Languages.ENGLISH.code)
    ) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            mutableCombinedWeatherData.value = Response.Loading
            try {
                val lat = favouriteLocation.location.latitude
                val lon = favouriteLocation.location.longitude

                val weatherDeferred = async{ repository.getWeatherLatLon(lat, lon, units, lang) }
                val forecastDeferred = async { repository.getForecastLatLon(lat, lon, units, lang) }

                val weather = weatherDeferred.await().firstOrNull()
                val forecast = forecastDeferred.await().firstOrNull()

                if(weather != null && forecast != null){
                    val data = CombinedWeatherData(weather, forecast)
                    mutableCombinedWeatherData.value = Response.Success(data)
                    updateCurrentLocation(favouriteLocation.copy(combinedWeatherData = data))
                }
                else{
                    mutableCombinedWeatherData.value = Response.Error("No Data Found")
                }

            } catch (exception: Exception) {
                mutableCombinedWeatherData.value = Response.Error(exception.message ?: "Unknown error occurred")
            }
        }
    }


    private fun updateCurrentLocation(favLocation: FavouriteLocation){
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("TAG", "updateCurrentLocation: ${favLocation.cityName}")
            val result = repository.updateFavoriteLocation(favLocation)
            Log.i("TAG", "updateCurrentLocation: $result")
        }
    }

}

class DetailsViewModelFactory(val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailsViewModel(repository) as T
    }

}