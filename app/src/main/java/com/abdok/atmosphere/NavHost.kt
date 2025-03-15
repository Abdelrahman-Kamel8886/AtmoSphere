package com.abdok.atmosphere

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.abdok.atmosphere.Data.Models.ScreenRoutes


@Composable
fun setupNavHost(navController: NavHostController) {

    NavHost(navController = navController, startDestination = ScreenRoutes.HomeRoute) {

        composable<ScreenRoutes.HomeRoute> {
            HomeScreen()
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
fun HomeScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Home Screen", fontSize = 24.sp)
    }
}

@Composable
fun LocationsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Locations Screen", fontSize = 24.sp)
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



