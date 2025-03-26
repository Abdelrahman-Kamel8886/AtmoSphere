package com.abdok.atmosphere.View.Screens.Settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.abdok.atmosphere.Data.Repository.Repository
import com.abdok.atmosphere.Enums.Languages
import com.abdok.atmosphere.Enums.Locations
import com.abdok.atmosphere.Enums.Speeds
import com.abdok.atmosphere.Enums.Units
import com.abdok.atmosphere.Utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(private val repository: Repository) : ViewModel() {

    private val _language = MutableStateFlow(Languages.ENGLISH.value)
    val language = _language.asStateFlow()

    private val _temperature = MutableStateFlow(Units.METRIC.value)
    val temperature = _temperature.asStateFlow()

    private val _location = MutableStateFlow(Locations.Gps.value)
    val location = _location.asStateFlow()

    private val _windSpeed = MutableStateFlow(Speeds.METERS_PER_SECOND.degree)
    val windSpeed = _windSpeed.asStateFlow()

    init {
        val code = repository.fetchPreferenceData(Constants.LANGUAGE_CODE , Languages.ENGLISH.code)
        _language.value = Languages.getValueByCode(code)

        val unit = repository.fetchPreferenceData(Constants.TEMPERATURE_UNIT , Units.METRIC.value)
        _temperature.value = Units.getDegreeByValue(unit)

        val loc = repository.fetchPreferenceData(Constants.LOCATION , Locations.Gps.value)
        _location.value = Locations.getValue(loc)

        val speed = repository.fetchPreferenceData(Constants.WIND_SPEED_UNIT , Speeds.METERS_PER_SECOND.degree)
        _windSpeed.value = Speeds.getDegree(speed)
    }

    fun updateLanguage(language: String) {
        val code = Languages.getCodeByValue(language)
        repository.savePreferenceData(Constants.LANGUAGE_CODE , code)
    }
    fun updateLocation(location: String) {
        _location.value = location
        repository.savePreferenceData(Constants.LOCATION , Locations.getEnglishValue(location))
    }

    fun updateTemperature(temperature: String) {
        _temperature.value = temperature
        repository.savePreferenceData(Constants.TEMPERATURE_UNIT , Units.getValueByDegree(temperature))

        if(Units.getValueByDegree(temperature) == Units.IMPERIAL.value){
            automaticUpdateWindSpeed(Speeds.getDegree(Speeds.MILES_PER_HOUR.degree))
        }
        else{
            automaticUpdateWindSpeed(Speeds.getDegree(Speeds.METERS_PER_SECOND.degree))
        }
    }

    fun updateWindSpeed(windSpeed: String) {
        _windSpeed.value = windSpeed
        repository.savePreferenceData(Constants.WIND_SPEED_UNIT , Speeds.getEnglishDegree(windSpeed))

        if(Speeds.getEnglishDegree(windSpeed) == Speeds.MILES_PER_HOUR.degree){
            automaticUpdateTemperature(Units.IMPERIAL.value)
        }
        else{
            automaticUpdateTemperature(Units.METRIC.value)
        }

    }

    private fun automaticUpdateWindSpeed(Speed: String){
        if (_windSpeed.value != Speed){
            _windSpeed.value = Speed
            repository.savePreferenceData(Constants.WIND_SPEED_UNIT , Speeds.getEnglishDegree(Speed))
        }
    }

    private fun automaticUpdateTemperature(temperature: String){
        if(temperature == Units.METRIC.value &&
            (_temperature.value == Units.getDegreeByValue(Units.STANDARD.value) || _temperature.value == Units.getDegreeByValue(Units.METRIC.value))){
            return
        }
        _temperature.value = Units.getDegreeByValue(temperature)
        repository.savePreferenceData(Constants.TEMPERATURE_UNIT , temperature)
    }

}

class SettingsViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(repository) as T
    }
}