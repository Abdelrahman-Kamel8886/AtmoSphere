package com.abdok.atmosphere.Ui

import androidx.annotation.ColorRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.abdok.atmosphere.Data.ScreenMenuItem
import com.abdok.atmosphere.R
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem
import np.com.susanthapa.curved_bottom_navigation.CurvedBottomNavigationView

@Composable
fun CurvedNavBar(navController: NavHostController) {

    val color = MaterialTheme.colorScheme.error.toArgb()

    AndroidView(
        factory = { context ->
            CurvedBottomNavigationView(context).apply {

                val cbnMenuItems = ScreenMenuItem.menuItems.map { screen ->
                    CbnMenuItem(
                        icon = screen.icon,
                        avdIcon = screen.selectedIcon,
                        destinationId = screen.id
                    )
                }
                setMenuItems(cbnMenuItems.toTypedArray(), 0)
                setOnMenuItemClickListener{ cbnMenuItem, i ->
                    navController.popBackStack()
                    navController.navigate(ScreenMenuItem.menuItems[i].route)
                }
                navBackgroundColor = color

            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
    )
}