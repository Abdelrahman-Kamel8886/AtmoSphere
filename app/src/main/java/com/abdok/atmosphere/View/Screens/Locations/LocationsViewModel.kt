package com.abdok.atmosphere.View.Screens.Locations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.abdok.atmosphere.Data.Models.FavouriteLocation
import com.abdok.atmosphere.Data.Repository.Repository
import com.abdok.atmosphere.Data.Response
import com.abdok.atmosphere.View.Screens.Home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LocationsViewModel(private val repository: Repository) : ViewModel()  {

    private val _favLocations = MutableStateFlow<Response<List<FavouriteLocation>>>(Response.Loading)
    val favLocations = _favLocations.asStateFlow()

    suspend fun getFavouriteLocations(){
        try {
            viewModelScope.launch(Dispatchers.IO){

                repository.getFavoriteLocations()
                    .catch {
                        _favLocations.value = Response.Error(it.message.toString())
                    }
                    .collect{
                        _favLocations.value = Response.Success(it)
                    }

            }
        }catch (e : Exception){
            _favLocations.value = Response.Error(e.message.toString())
        }
    }

}

class LocationViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationsViewModel(repository) as T
    }
}