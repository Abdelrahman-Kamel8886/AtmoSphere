package com.abdok.atmosphere.View.Screens.Map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.abdok.atmosphere.Data.Models.CityLocationResponseItem
import com.abdok.atmosphere.Data.Models.CombinedWeatherData
import com.abdok.atmosphere.Data.Models.FavouriteLocation
import com.abdok.atmosphere.Data.Repository.Repository
import com.abdok.atmosphere.Data.Response
import com.abdok.atmosphere.Enums.MapSelection
import com.abdok.atmosphere.Utils.Constants
import com.abdok.atmosphere.View.Screens.Home.HomeViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MapViewModel(private val repository: Repository) : ViewModel() {

    private val _addressLocation =
        MutableStateFlow<Response<Pair<CityLocationResponseItem, Boolean>>>(Response.Loading)
    val addressLocation = _addressLocation.asStateFlow()

    private val _insertionState = MutableStateFlow<Response<Boolean>?>(null)
    val insertionState = _insertionState.asStateFlow()

    private var mutableMessage = MutableSharedFlow<String>()
    val message = mutableMessage.asSharedFlow()


    fun getCityLocation(cityName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCityLocation(cityName).catch {
                _addressLocation.value = Response.Error(it.message.toString())
            }.collect {
                _addressLocation.value = Response.Success(it[0] to true)
            }
        }
    }

    fun getCityName(latLng: LatLng) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCityName(latLng.latitude, latLng.longitude).catch {
                _addressLocation.value = Response.Error(it.message.toString())
            }.collect {
                _addressLocation.value = Response.Success(it[0] to false)
            }
        }
    }

    fun selectLocation(
        cityName: String,
        latLng: LatLng,
        mapSelection: MapSelection = MapSelection.FAVOURITE
    ) {
        _insertionState.value = Response.Loading
        when (mapSelection) {
            MapSelection.FAVOURITE -> {
                getCombinedData(
                    cityName,
                    latLng
                )
            }

            MapSelection.LOCATION -> {}
        }
    }

    private fun getCombinedData(
        cityName: String,
        latLng: LatLng,
        units: String = Constants.unit,
        lang: String = Constants.DEFAULT_LANG
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val weatherDeferred = async { repository.getWeatherLatLon(latLng.latitude, latLng.longitude, units, lang) }
                val forecastDeferred = async { repository.getForecastLatLon(latLng.latitude, latLng.longitude, units, lang) }

                val weather = weatherDeferred.await()
                val forecast = forecastDeferred.await()

                insertFavoriteLocation(cityName, latLng, CombinedWeatherData(weather , forecast))


            } catch (exception: Exception) {
                _insertionState.value = Response.Error(exception.message.toString())
                mutableMessage.emit(exception.message.toString())
            }
        }
    }


    private fun insertFavoriteLocation(
        cityName: String,
        latLng: LatLng,
        combinedData: CombinedWeatherData
    ) {
        val favorite = FavouriteLocation(
            name = cityName
            , location = latLng
            , combinedWeatherData = combinedData
        )
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.insertFavoriteLocation(favorite)
                if (result > 0) {
                    _insertionState.value = Response.Success(true)
                } else {
                    _insertionState.value = Response.Error("This location already exist")
                    mutableMessage.emit("This location already exist")
                }
            } catch (e: Exception) {
                _insertionState.value = Response.Error(e.message.toString())
                mutableMessage.emit(e.message.toString())
            }

        }
    }


}

class MapViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapViewModel(repository) as T
    }
}
