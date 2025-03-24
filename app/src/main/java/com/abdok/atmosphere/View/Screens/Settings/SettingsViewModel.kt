package com.abdok.atmosphere.View.Screens.Settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.abdok.atmosphere.Data.Repository.Repository
import com.abdok.atmosphere.Enums.Languages
import com.abdok.atmosphere.Utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(private val repository: Repository) : ViewModel() {

    private val _language = MutableStateFlow(Languages.ENGLISH.value)
    val language = _language.asStateFlow()

    init {
        val code = repository.fetchPreferenceData(Constants.LANGUAGE_CODE , Languages.ENGLISH.code)
        _language.value = Languages.getValueByCode(code)
    }

    fun updateLanguage(language: String) {
        val code = Languages.getCodeByValue(language)
        repository.savePreferenceData(Constants.LANGUAGE_CODE , code)
    }

}

class SettingsViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(repository) as T
    }
}