package com.abdok.atmosphere.ui.Screens.Details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abdok.atmosphere.Data.Models.CombinedWeatherData
import com.abdok.atmosphere.Data.Models.FavouriteLocation
import com.abdok.atmosphere.Data.Response
import com.abdok.atmosphere.R
import com.abdok.atmosphere.ui.CurvedNavBar
import com.abdok.atmosphere.ui.Screens.Home.DrawHome
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(favouriteLocation: FavouriteLocation , viewModel: DetailsViewModel) {

    CurvedNavBar.mutableNavBarState.value = false
    val context = LocalContext.current

    LaunchedEffect(Unit){
        viewModel.getWeatherAndForecast(favouriteLocation)
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
                    viewModel.getWeatherAndForecast(favouriteLocation)
                }
            },
        ) {
            when (weatherDataState.value) {
                is Response.Loading -> {
                }
                is Response.Error -> {
                    val message = (weatherDataState.value as Response.Error).exception
                    DrawDetails(favouriteLocation.combinedWeatherData)
                }

                is Response.Success -> {
                    val data = (weatherDataState.value as Response.Success).data
                    DrawDetails(data)
                }

                null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = stringResource(R.string.check_your_internet_connection), color = Color.Red)
                    }
                }
            }
        }
    }
}

@Composable
fun DrawDetails(data: CombinedWeatherData) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            DrawHome(combinedWeatherData = data)
        }
    }
}
