package com.abdok.atmosphere.data.models

import com.abdok.atmosphere.enums.MapSelection
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

    @Serializable
    data class DetailsRoute(val favouriteLocation: String) : ScreenRoutes("details")

}