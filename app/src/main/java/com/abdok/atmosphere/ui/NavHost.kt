package com.abdok.atmosphere.ui

import android.location.Location
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.abdok.atmosphere.R
import com.abdok.atmosphere.data.RepositoryImpl
import com.abdok.atmosphere.data.local.LocalDataSourceImpl
import com.abdok.atmosphere.data.local.room.LocalDataBase
import com.abdok.atmosphere.data.local.sharedPreference.SharedPreferencesImpl
import com.abdok.atmosphere.data.models.FavouriteLocation
import com.abdok.atmosphere.data.models.ScreenRoutes
import com.abdok.atmosphere.data.remote.RemoteDataSourceImpl
import com.abdok.atmosphere.data.remote.retrofit.RetroConnection
import com.abdok.atmosphere.enums.MapSelection
import com.abdok.atmosphere.ui.screens.Locations.LocationViewModelFactory
import com.abdok.atmosphere.ui.screens.Locations.LocationsScreen
import com.abdok.atmosphere.ui.screens.Locations.LocationsViewModel
import com.abdok.atmosphere.ui.screens.alarm.AlarmViewModel
import com.abdok.atmosphere.ui.screens.alarm.AlarmViewModelFactory
import com.abdok.atmosphere.ui.screens.alarm.AlertsScreen
import com.abdok.atmosphere.ui.screens.details.DetailsScreen
import com.abdok.atmosphere.ui.screens.details.DetailsViewModel
import com.abdok.atmosphere.ui.screens.details.DetailsViewModelFactory
import com.abdok.atmosphere.ui.screens.home.HomeScreen
import com.abdok.atmosphere.ui.screens.home.HomeViewModel
import com.abdok.atmosphere.ui.screens.home.HomeViewModelFactory
import com.abdok.atmosphere.ui.screens.map.MapScreen
import com.abdok.atmosphere.ui.screens.map.MapViewModel
import com.abdok.atmosphere.ui.screens.map.MapViewModelFactory
import com.abdok.atmosphere.ui.screens.settings.SettingsScreen
import com.abdok.atmosphere.ui.screens.settings.SettingsViewModel
import com.abdok.atmosphere.ui.screens.settings.SettingsViewModelFactory
import com.abdok.atmosphere.utils.extension.isNetworkConnected

@Composable
fun SetupNavHost(navController: NavHostController, location: Location?) {

    val context = LocalContext.current

    NavHost(navController = navController
        , startDestination = ScreenRoutes.HomeRoute
    ) {

        val repository = RepositoryImpl.getInstance(
            RemoteDataSourceImpl.getInstance(RetroConnection.retroServices),
            LocalDataSourceImpl.getInstance(LocalDataBase.getInstance().localDao(), SharedPreferencesImpl.getInstance())
        )

        composable<ScreenRoutes.HomeRoute> {
            val homeFactory = HomeViewModelFactory(repository)
            val viewModel: HomeViewModel = viewModel(factory = homeFactory)
            HomeScreen(viewModel, location)
        }
        composable<ScreenRoutes.LocationsRoute> {

            val locationFactory = LocationViewModelFactory(repository)
            val viewModel: LocationsViewModel = viewModel(factory = locationFactory)

            LocationsScreen(
                viewModel,
                onLocationClick = {
                    if (context.isNetworkConnected()){
                        navController.navigate(ScreenRoutes.MapRoute(MapSelection.FAVOURITE))
                    }
                    else{
                        Toast.makeText(context, context.getString(R.string.check_your_internet_connection), Toast.LENGTH_SHORT).show()
                    }
                },
                onItemSelected = {
                    navController.navigate(ScreenRoutes.DetailsRoute(it))
                }
            )
        }
        composable<ScreenRoutes.AlertsRoute> {
            val alertsFactory = AlarmViewModelFactory(repository)
            val viewModel: AlarmViewModel = viewModel(factory = alertsFactory)
            AlertsScreen(viewModel)
        }
        composable<ScreenRoutes.SettingsRoute> {
            val settingsFactory = SettingsViewModelFactory(repository)
            val viewModel: SettingsViewModel = viewModel(factory = settingsFactory)
            SettingsScreen(viewModel){
                if (context.isNetworkConnected()){
                    navController.navigate(ScreenRoutes.MapRoute(MapSelection.LOCATION))
                }
                else{
                    Toast.makeText(context, context.getString(R.string.check_your_internet_connection), Toast.LENGTH_SHORT).show()
                }
            }
        }
        composable<ScreenRoutes.MapRoute> {
            val args : ScreenRoutes.MapRoute = it.toRoute()
            val mapFactory = MapViewModelFactory(repository)
            val viewModel: MapViewModel = viewModel(factory = mapFactory)
            MapScreen(viewModel , args.mapSelection) {
                navController.popBackStack()
            }
        }

        composable<ScreenRoutes.DetailsRoute> {
            val args : ScreenRoutes.DetailsRoute = it.toRoute()
            val favLocationModel = FavouriteLocation.fromJson(args.favouriteLocation)
            val detailsFactory = DetailsViewModelFactory(repository)
            val viewModel: DetailsViewModel = viewModel(factory = detailsFactory)
            DetailsScreen(favLocationModel , viewModel)
        }
    }
}


