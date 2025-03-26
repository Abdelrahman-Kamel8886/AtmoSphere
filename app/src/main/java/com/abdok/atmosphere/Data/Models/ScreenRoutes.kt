package com.abdok.atmosphere.Data.Models

import com.abdok.atmosphere.Enums.MapSelection
import kotlinx.serialization.Serializable


@Serializable
sealed class ScreenRoutes(val route: String) {

    @Serializable
    object HomeRoute : ScreenRoutes("home")

    @Serializable
    object LocationsRoute : ScreenRoutes("locations")

    @Serializable
    object AlertsRoute : ScreenRoutes("alerts")

    @Serializable
    object SettingsRoute : ScreenRoutes("settings")

    @Serializable
    data class MapRoute(val mapSelection: MapSelection) : ScreenRoutes("map")

}