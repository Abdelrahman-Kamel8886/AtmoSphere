package com.abdok.atmosphere.Data.Models

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
    object MapRoute : ScreenRoutes("map")

}