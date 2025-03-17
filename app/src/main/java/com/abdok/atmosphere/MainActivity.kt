package com.abdok.atmosphere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.abdok.atmosphere.Ui.Screens.CurvedNavBar
import com.abdok.atmosphere.Utils.BackgroundMapper
import com.abdok.atmosphere.Utils.SharedModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
           MainScreen()
        }
    }
}

@Composable
fun MainScreen() {

    val defaultBackground = Brush.linearGradient(
        listOf(Color(0xFFF5F5F5), Color(0xFFFFFFFF))
    )

    val navController = rememberNavController()
    //var background = SharedModel.screenBackground.observeAsState()
    Scaffold(
        bottomBar = {
            CurvedNavBar(navController)
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .background(
                defaultBackground
            )
        ) {
            setupNavHost(navController)
        }
    }
}







