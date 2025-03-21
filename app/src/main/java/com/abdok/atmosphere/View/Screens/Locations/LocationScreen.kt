package com.abdok.atmosphere.View.Screens.Locations

import android.location.Location
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abdok.atmosphere.Data.Response
import com.abdok.atmosphere.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun LocationsScreen(
    onLocationClick: () -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        containerColor = Color.White,
        floatingActionButton = { FloatingActionButton(onClick = {
            onLocationClick()
        }, containerColor = Color.DarkGray , modifier = Modifier.padding(bottom = 56.dp )){
            Icon(painter = painterResource(R.drawable.baseline_add_24), tint = Color.White, contentDescription = "add") }
        },floatingActionButtonPosition = FabPosition.End,

    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding), contentAlignment = Alignment.Center) {
            Text(text = "Locations Screen", fontSize = 24.sp)
        }
    }
}

