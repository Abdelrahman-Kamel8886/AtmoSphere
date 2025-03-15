package com.abdok.atmosphere.Ui.Home

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.abdok.atmosphere.Data.DataSources.RemoteDataSource
import com.abdok.atmosphere.Data.Remote.RetroConnection
import com.abdok.atmosphere.Repository

import androidx.lifecycle.viewmodel.compose.*


@Composable
fun HomeScreen(){

    val homeFactory = HomeViewModelFactory(Repository.getInstance(
        RemoteDataSource.getInstance(
            RetroConnection.retroServices)))

    val viewModel: HomeViewModel = viewModel(factory = homeFactory)

    viewModel.getWeatherAndForecastLatLon(30.666733, 31.169271)

    val combinedWeatherData = viewModel.combinedWeatherData.observeAsState()
    val messageState = viewModel.error.observeAsState()



    Log.d("TAG", "HomeScreen weather: ${combinedWeatherData.value?.weatherResponse.toString()}")
    Log.d("TAG", "HomeScreen Forecast: ${combinedWeatherData.value?.forecastResponse.toString()}")

    messageState.value.let {
        Log.e("TAG", "HomeScreen error: $it")
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Home Screen ${combinedWeatherData.value?.forecastResponse?.city?.name}",
            fontSize = 24.sp
        )
    }
}
