package com.abdok.atmosphere

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.abdok.atmosphere.ui.theme.AtmoSphereTheme
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem
import np.com.susanthapa.curved_bottom_navigation.CurvedBottomNavigationView


val menuItems = listOf(
    Screen("home", R.drawable.ic_baseline_home_24, R.drawable.ic_baseline_home_avd, 1),
    Screen("locations", R.drawable.baseline_add_location_alt_24, R.drawable.ic_baseline_loc_avd,2),
    Screen("notifications", R.drawable.baseline_add_alert_24, R.drawable.ic_baseline_alert_avd,3),
    Screen("settings", R.drawable.baseline_settings_24, R.drawable.ic_baseline_settings_avd,4),)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
           MainScreen()
        }
    }
}

@Preview
@Composable
fun MainScreen() {
    var selectedRoute = remember { mutableStateOf(menuItems[0].route) }

    Scaffold(
        bottomBar = {
            /*AndroidView(factory = {
                CurvedBottomNavigationView(context = it).apply {
                    val cbnMenuItems = menuItems.map { screen ->
                        CbnMenuItem(
                            icon = screen.icon,
                            avdIcon = screen.selectedIcon,
                            destinationId = screen.id
                        )
                    }
                    setMenuItems(cbnMenuItems.toTypedArray(), 0)
                    setOnMenuItemClickListener{ cbnMenuItem, i ->
                        selectedRoute.value = menuItems[i].route
                    }
                }
            }, Modifier.fillMaxWidth())*/
            CurvedNavBar{ route ->
                selectedRoute.value = route
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = selectedRoute.value , fontSize = 24.sp)
        }
    }
}


@Composable
fun CurvedNavBar(
    onItemSelected: (String) -> Unit
) {
    AndroidView(
        factory = { context ->
            CurvedBottomNavigationView(context).apply {

                val cbnMenuItems = menuItems.map { screen ->
                    CbnMenuItem(
                        icon = screen.icon,
                        avdIcon = screen.selectedIcon,
                        destinationId = screen.id
                    )
                }
                setMenuItems(cbnMenuItems.toTypedArray(), 0)

                setOnMenuItemClickListener{ cbnMenuItem, i ->
                    onItemSelected(menuItems[i].route)
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
    )
}

