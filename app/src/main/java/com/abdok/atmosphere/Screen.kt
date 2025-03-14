package com.abdok.atmosphere

import androidx.annotation.DrawableRes

data class Screen(
    val route: String,
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int,
    val id: Int
)
