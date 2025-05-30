package com.abdok.atmosphere.screens.Locations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.abdok.atmosphere.data.repository.Repository
import com.abdok.atmosphere.data.models.FavouriteLocation
import com.abdok.atmosphere.data.models.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LocationsViewModel(private val repository: Repository) : ViewModel() {

    private val _favLocations =
        MutableStateFlow<Response<List<FavouriteLocation>>>(Response.Loading)
    val favLocations = _favLocations.asStateFlow()

    private var _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    fun getFavouriteLocations() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _favLocations.value = Response.Loading
                repository.getFavoriteLocations()
                    .catch {
                        _favLocations.value = Response.Error(it.message.toString())
                    }
                    .collect {
                        _favLocations.value = Response.Success(it)
                    }

            } catch (e: Exception) {
                _favLocations.value = Response.Error(e.message.toString())
            }
        }
    }

    fun deleteFavouriteLocation(favoriteLocation: FavouriteLocation) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.deleteFavoriteLocation(favoriteLocation)
                if (result > 0) {
                    _message.emit("Location Deleted Successfully")
                } else {
                    _message.emit("Something went wrong")
                }
            } catch (e: Exception) {
                _message.emit(e.message.toString())
            }
        }


    }

}

class LocationViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationsViewModel(repository) as T
    }
}