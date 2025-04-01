package com.abdok.atmosphere.screens.map

import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.abdok.atmosphere.data.repository.Repository
import com.abdok.atmosphere.data.models.CityLocationResponseItem
import com.abdok.atmosphere.data.models.CombinedWeatherData
import com.abdok.atmosphere.data.models.FavouriteLocation
import com.abdok.atmosphere.data.models.Response
import com.abdok.atmosphere.enums.Languages
import com.abdok.atmosphere.enums.Locations
import com.abdok.atmosphere.enums.MapSelection
import com.abdok.atmosphere.enums.Units
import com.abdok.atmosphere.utils.Constants
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MapViewModel(private val repository: Repository) : ViewModel() {

    private val _addressLocation = MutableStateFlow<Response<Pair<CityLocationResponseItem, Boolean>>>(
        Response.Loading)
    val addressLocation = _addressLocation.asStateFlow()

    private val _insertionState = MutableStateFlow<Response<Boolean>?>(null)
    val insertionState = _insertionState.asStateFlow()

    private var mutableMessage = MutableSharedFlow<String>()
    val message = mutableMessage.asSharedFlow()

    fun getCityLocation(cityName: String) {
            viewModelScope.launch(Dispatchers.IO) {
                var secondTry = false
                repository.getCityLocation(cityName).catch {
                    _addressLocation.value = Response.Error(it.message.toString())
                }.collect {
                    if (it.isEmpty() ) {
                        if (secondTry){
                            _addressLocation.value = Response.Error("No result found")
                        }
                        else{
                            getCityLocation(cityName.substringBefore(","))
                            secondTry = true
                        }
                    }else
                    _addressLocation.value = Response.Success(it[0] to true)
                }
            }
    }

    fun getLatLonByPlaceId(placesClient :PlacesClient , autocompletePlace: AutocompletePrediction) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val placeFields = listOf(Place.Field.LAT_LNG)
                val request = FetchPlaceRequest.builder(autocompletePlace.placeId, placeFields).build()
                val response = Tasks.await(placesClient.fetchPlace(request))

                val latLng = response.place.latLng
                if (latLng != null) {
                    val cityInfo = CityLocationResponseItem(lat = latLng.latitude, lon = latLng.longitude,
                        country = autocompletePlace.getSecondaryText(null).toString(),
                        name = autocompletePlace.getFullText(null).toString(),
                        local_names = null,
                        state = autocompletePlace.getPrimaryText(null).toString()
                    )
                    _addressLocation.value = Response.Success(cityInfo to true)
                } else {
                    _addressLocation.value = Response.Error("No LatLng found for the place")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _addressLocation.value = Response.Error(e.message.toString())
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

    fun getCityNameFromLatLng(geocoder: Geocoder , latLng: LatLng){
        return try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            val cityInfo = CityLocationResponseItem(lat = latLng.latitude, lon = latLng.longitude,
                country = addresses?.get(0)?.countryName.toString(), name = addresses?.get(0)?.locality.toString(),
                local_names = null, state = addresses?.get(0)?.adminArea.toString()
            )
            _addressLocation.value = Response.Success(cityInfo to false)

        } catch (e: Exception) {
            _addressLocation.value = Response.Error(e.message.toString())
        }
    }

    fun selectLocation(
        cityName: String,
        latLng: LatLng,
        mapSelection: MapSelection
    ) {
        _insertionState.value = Response.Loading
        when (mapSelection) {
            MapSelection.FAVOURITE -> {
                getCombinedData(
                    cityName,
                    latLng
                )
            }
            MapSelection.LOCATION -> {
                saveMapSelection(latLng)
            }
        }
    }

    fun saveMapSelection(latLng: LatLng) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveLocation(Locations.Map , latLng.latitude , latLng.longitude)
            repository.savePreferenceData(Constants.LOCATION , Locations.Map.value)
            _insertionState.value = Response.Success(true)
        }
    }

    private fun getCombinedData(
        cityName: String,
        latLng: LatLng,
        units: String = repository.fetchPreferenceData(Constants.TEMPERATURE_UNIT , Units.METRIC.value),
        lang: String = repository.fetchPreferenceData(Constants.LANGUAGE_CODE , Languages.ENGLISH.code)
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val weatherDeferred = async { repository.getWeatherLatLon(latLng.latitude, latLng.longitude, units, lang) }
                val forecastDeferred = async { repository.getForecastLatLon(latLng.latitude, latLng.longitude, units, lang) }

                val weather = weatherDeferred.await().firstOrNull()
                val forecast = forecastDeferred.await().firstOrNull()

                if (weather != null && forecast != null) {
                    insertFavoriteLocation(cityName, latLng, CombinedWeatherData(weather , forecast))
                }




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
            cityName = cityName
            , countryName =combinedData.weatherResponse.sys.country
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
