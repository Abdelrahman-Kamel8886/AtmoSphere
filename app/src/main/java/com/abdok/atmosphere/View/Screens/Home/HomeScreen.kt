package com.abdok.atmosphere.View.Screens.Home

import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abdok.atmosphere.Data.Models.CombinedWeatherData
import com.abdok.atmosphere.Data.Response
import com.abdok.atmosphere.R
import com.abdok.atmosphere.Utils.CountryHelper
import com.abdok.atmosphere.Utils.SharedModel
import com.abdok.atmosphere.Utils.ViewHelpers.BackgroundMapper
import com.abdok.atmosphere.View.Screens.Home.Components.DaysForecastList
import com.abdok.atmosphere.View.Screens.Home.Components.HourlyForecastList
import com.abdok.atmosphere.View.Screens.Home.Components.SunCycleView
import com.abdok.atmosphere.View.Screens.Home.Components.WeatherCard
import com.abdok.atmosphere.View.Screens.Home.Components.WeatherGrid
import com.abdok.atmosphere.View.Screens.Home.Components.WindCard
import com.abdok.atmosphere.View.Theme.ColorTextSecondary
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel, location: Location?) {
    LaunchedEffect(Unit) {
        location?.let {
            viewModel.updateCurrentLocation(location.latitude, location.longitude)
        }
        viewModel.getWeatherAndForecastLatLon()
    }

    val weatherDataState = viewModel.combinedWeatherData.collectAsStateWithLifecycle()
    val refreshState = rememberPullToRefreshState()

    val isRefreshing = weatherDataState.value is Response.Loading

    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        PullToRefreshBox(
            modifier = Modifier.fillMaxSize(),
            state = refreshState,
            isRefreshing = isRefreshing,
            onRefresh = {
                scope.launch {
                    viewModel.getWeatherAndForecastLatLon()
                }
            },
        ) {
            when (weatherDataState.value) {
                is Response.Loading -> {

                }

                is Response.Error -> {
                    val message = (weatherDataState.value as Response.Error).exception
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = message, color = Color.Red)
                    }
                }

                is Response.Success -> {
                    val data = (weatherDataState.value as Response.Success).data
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        item {
                            DrawHome(combinedWeatherData = data)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DrawHome(combinedWeatherData: CombinedWeatherData) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Background GIF
      /*  GifEffectBackground(
            modifier = Modifier
                .matchParentSize()
                .zIndex(0f) // Set background layer to lowest
        )*/

        Column(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f)
        ) {
            combinedWeatherData.weatherResponse?.let {

                val cardBrush = BackgroundMapper.getCardBackground(it.weather[0].icon)
                val screenBrush = BackgroundMapper.getScreenBackground(it.weather[0].icon)
                ColorTextSecondary = BackgroundMapper.getTextColor(it.weather[0].icon)
                SharedModel.screenBackground.value = screenBrush

                Column {
                    Spacer(modifier = Modifier.height(32.dp))
                    TopView(it.name, CountryHelper.getCountryNameFromCode(it.sys.country) ?: "")
                    Spacer(modifier = Modifier.height(8.dp))
                    WeatherCard(it)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.today), modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp), fontWeight = FontWeight.Bold, fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    HourlyForecastList(combinedWeatherData?.forecastResponse!!)
                    Spacer(modifier = Modifier.height(16.dp))
                    WindCard(
                        brush = cardBrush,
                        value = it.wind.speed,
                        degree = it.wind.deg.toFloat()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    WeatherGrid(
                        cardBrush,
                        humidity = "${it.main.humidity} %",
                        visibility = "${it.visibility} ${stringResource(R.string.m)}",
                        pressure = "${it.main.pressure} ${stringResource(R.string.hpa)}"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    SunCycleView(
                        brush = cardBrush,
                        sunRise = it.sys.sunrise.toLong(),
                        sunSet = it.sys.sunset.toLong()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string._5_days_forecast), modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp), fontWeight = FontWeight.Bold, fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    DaysForecastList(
                        brush = cardBrush,
                        forecast = combinedWeatherData?.forecastResponse!!
                    )
                    Spacer(modifier = Modifier.height(100.dp))
                }

            }
        }
    }
}


@Composable
fun TopView(
    city: String = "Zefta",
    country: String = "Egypt"
) {

    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 48.dp, start = 16.dp)
    ) {

        Text(
            text = "$city,",
            fontWeight = FontWeight.SemiBold, fontSize = 30.sp
        )

        Text(
            text = country,
            fontWeight = FontWeight.SemiBold, fontSize = 30.sp
        )


    }
}

























