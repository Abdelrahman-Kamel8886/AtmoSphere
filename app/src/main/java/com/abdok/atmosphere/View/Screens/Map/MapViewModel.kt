package com.abdok.atmosphere.View.Screens.Map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.abdok.atmosphere.Data.Models.CityLocationResponseItem
import com.abdok.atmosphere.Data.Repository.Repository
import com.abdok.atmosphere.Data.Response
import com.abdok.atmosphere.View.Screens.Home.HomeViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MapViewModel(private val repository: Repository) : ViewModel() {


    private val _addressLocation =  MutableStateFlow<Response<CityLocationResponseItem>>(Response.Loading)
    val addressLocation = _addressLocation.asStateFlow()

    fun getCityLocation(cityName: String){

        viewModelScope.launch(Dispatchers.IO){

            repository.getCityLocation(cityName).
                    catch {
                        _addressLocation.value = Response.Error(it.message.toString())
                    }
                .collect{
                    _addressLocation.value = Response.Success(it[0])
                }

        }

    }

}

class MapViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapViewModel(repository) as T
    }
}
