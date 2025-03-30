package com.abdok.atmosphere.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.abdok.atmosphere.data.RepositoryImpl
import com.abdok.atmosphere.enums.Languages
import com.abdok.atmosphere.enums.Locations
import com.abdok.atmosphere.enums.Speeds
import com.abdok.atmosphere.enums.Units
import com.abdok.atmosphere.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class SettingsViewModel(private val repository: RepositoryImpl) : ViewModel() {

    private val _language = MutableStateFlow(Languages.ENGLISH.value)
    val language = _language.asStateFlow()

    private val _temperature = MutableStateFlow(Units.METRIC.value)
    val temperature = _temperature.asStateFlow()

    private val _location = MutableStateFlow(Locations.Gps.value)
    val location = _location.asStateFlow()

    private val _windSpeed = MutableStateFlow(Speeds.METERS_PER_SECOND.degree)
    val windSpeed = _windSpeed.asStateFlow()


    fun refreshValues(){
        val code = repository.fetchPreferenceData(Constants.LANGUAGE_CODE , Locale.getDefault().language)
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

class SettingsViewModelFactory(private val repository: RepositoryImpl) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(repository) as T
    }
}