package com.abdok.atmosphere

import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.abdok.atmosphere.Data.DataSources.RemoteDataSource
import com.abdok.atmosphere.Data.Models.ScreenRoutes
import com.abdok.atmosphere.Data.Remote.RetroConnection
import com.abdok.atmosphere.Data.Repository.Repository
import com.abdok.atmosphere.View.Screens.Home.HomeScreen
import com.abdok.atmosphere.View.Screens.Home.HomeViewModel
import com.abdok.atmosphere.View.Screens.Home.HomeViewModelFactory
import com.abdok.atmosphere.View.Screens.Locations.LocationsScreen


@Composable
fun setupNavHost(navController: NavHostController , location: Location) {

    NavHost(navController = navController, startDestination = ScreenRoutes.HomeRoute) {

        composable<ScreenRoutes.HomeRoute> {
            val homeFactory = HomeViewModelFactory(
                Repository.getInstance(
                    RemoteDataSource.getInstance(
                        RetroConnection.retroServices
                    )
                )
            )
            val viewModel: HomeViewModel = viewModel(factory = homeFactory)
            HomeScreen(viewModel , location)
        }
        composable<ScreenRoutes.LocationsRoute> {
            LocationsScreen()
        }
        composable<ScreenRoutes.AlertsRoute> {
            AlertsScreen()
        }
        composable<ScreenRoutes.SettingsRoute> {
            SettingsScreen()
        }
    }
}




@Composable
fun AlertsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Alerts Screen", fontSize = 24.sp)
    }
}

@Composable
fun SettingsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Settings Screen", fontSize = 24.sp)
    }
}



