package com.abdok.atmosphere.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.abdok.atmosphere.data.Repository
import com.abdok.atmosphere.enums.Locations
import com.abdok.atmosphere.utils.Constants
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