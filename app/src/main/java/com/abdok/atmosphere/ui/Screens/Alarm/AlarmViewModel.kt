package com.abdok.atmosphere.ui.Screens.Alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.abdok.atmosphere.Data.Models.AlertDTO
import com.abdok.atmosphere.Data.Repository
import com.abdok.atmosphere.Data.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlarmViewModel(private val repository: Repository) : ViewModel() {

    private val _alerts = MutableStateFlow<Response<List<AlertDTO>>>(Response.Loading)
    val alerts = _alerts.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    fun getAlerts() {
        viewModelScope.launch(Dispatchers.IO) {
            _alerts.value = Response.Loading
            repository.getAlerts()
                .catch {
                    _alerts.value = Response.Error(it.message.toString())
                }
                .collect {
                    _alerts.value = Response.Success(it)
                }
        }
    }

    fun addAlert(alert: AlertDTO) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.insertAlert(alert)
            if (result > 0) {
                _message.value = "Alert added successfully"
            } else {
                _message.value = "Failed to add alert"
            }
        }
    }

    fun deleteAlert(alert: AlertDTO) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.deleteAlert(alert)
            if (result > 0) {
                _message.value = "Alert deleted successfully"
            } else {
                _message.value = "Failed to delete alert"
            }
        }
    }


}


class AlarmViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlarmViewModel(repository) as T
    }
}

