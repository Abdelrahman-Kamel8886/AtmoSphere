package com.abdok.atmosphere.screens

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.abdok.atmosphere.data.local.sharedPreference.SharedPreferencesImpl
import com.abdok.atmosphere.data.repository.Repository
import com.abdok.atmosphere.enums.Locations
import com.abdok.atmosphere.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

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

    fun applyLanguage(context: Context) {
        val sharedPreferences = SharedPreferencesImpl.getInstance()
        val language = sharedPreferences.fetchData(Constants.LANGUAGE_CODE, Constants.DEFAULT_LANG)

        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}

class MainViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}