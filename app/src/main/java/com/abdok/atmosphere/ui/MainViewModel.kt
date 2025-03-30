package com.abdok.atmosphere.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.abdok.atmosphere.Data.Repository
import com.abdok.atmosphere.Enums.Locations
import com.abdok.atmosphere.Utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {

    val locationState = MutableLiveData<String>()

    init {
        getLocationSate()
    }

    private fun getLocationSate(){
        viewModelScope.launch(Dispatchers.IO) {
            locationState.postValue(repository.fetchPreferenceData(Constants.LOCATION , Locations.Gps.value))
        }
    }

}

class MainViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}