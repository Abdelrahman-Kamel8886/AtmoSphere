package com.abdok.atmosphere.ui.components

import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import com.abdok.atmosphere.data.models.ScreenMenuItem
import kotlinx.coroutines.flow.MutableStateFlow
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem
import np.com.susanthapa.curved_bottom_navigation.CurvedBottomNavigationView

object CurvedNavBar {

    val mutableNavBarState = MutableStateFlow(true)
    val activeIndex: MutableLiveData<Int> = MutableLiveData(0)


    @Composable
    fun ShowCurvedNavBar(navController: NavHostController) {
        val defaultBackground = Brush.linearGradient(
            listOf(Color(0xFFF5F5F5), Color(0xFFFFFFFF))
        )

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
                    layoutDirection = View.LAYOUT_DIRECTION_LTR
                    setMenuItems(cbnMenuItems.toTypedArray(), activeIndex.value ?: 0)
                    setOnMenuItemClickListener { cbnMenuItem, i ->
                        activeIndex.value = i
                        navController.popBackStack()
                        navController.navigate(ScreenMenuItem.menuItems[i].route)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(color = Color.Transparent)
        )
    }

}

