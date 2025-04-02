package com.abdok.atmosphere.data.models

import androidx.annotation.DrawableRes
import com.abdok.atmosphere.R

data class ScreenMenuItem(
    val route: ScreenRoutes,
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int,
    val id: Int
){
    companion object{
        val menuItems = listOf(
            ScreenMenuItem(ScreenRoutes.HomeRoute, R.drawable.ic_baseline_home_24, R.drawable.ic_baseline_home_avd, 1),
            ScreenMenuItem(ScreenRoutes.LocationsRoute, R.drawable.baseline_location_on_24, R.drawable.ic_baseline_loc_avd,2),
            ScreenMenuItem(ScreenRoutes.AlertsRoute, R.drawable.baseline_notifications_active_24, R.drawable.ic_baseline_alert_avd,3),
            ScreenMenuItem(ScreenRoutes.SettingsRoute, R.drawable.baseline_settings_24, R.drawable.ic_baseline_settings_avd,4)
        )
    }
}
