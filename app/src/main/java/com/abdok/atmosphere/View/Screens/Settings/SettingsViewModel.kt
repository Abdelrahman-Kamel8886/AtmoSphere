package com.abdok.atmosphere.View.Screens.Settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.abdok.atmosphere.Data.Repository.Repository
import com.abdok.atmosphere.Enums.Languages
import com.abdok.atmosphere.Enums.Units
import com.abdok.atmosphere.Utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(private val repository: Repository) : ViewModel() {

    private val _language = MutableStateFlow(Languages.ENGLISH.value)
    val language = _language.asStateFlow()

    private val _temperature = MutableStateFlow(Units.METRIC.value)
    val temperature = _temperature.asStateFlow()

    init {
        val code = repository.fetchPreferenceData(Constants.LANGUAGE_CODE , Languages.ENGLISH.code)
        _language.value = Languages.getValueByCode(code)

        val unit = repository.fetchPreferenceData(Constants.TEMPERATURE_UNIT , Units.METRIC.value)
        _temperature.value = Units.getDegreeByValue(unit)
    }

    fun updateLanguage(language: String) {
        val code = Languages.getCodeByValue(language)
        repository.savePreferenceData(Constants.LANGUAGE_CODE , code)
    }
    fun updateTemperature(temperature: String) {
        _temperature.value = temperature
        repository.savePreferenceData(Constants.TEMPERATURE_UNIT , Units.getValueByDegree(temperature))
    }

}

class SettingsViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(repository) as T
    }
}