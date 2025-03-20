package com.abdok.atmosphere.View.Screens.Locations

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

@Preview
@Composable
fun LocationsScreen() {
    val context = LocalContext.current
    Scaffold(
        floatingActionButton = { FloatingActionButton(onClick = {
            Toast.makeText(context, "Floating action button clicked", Toast.LENGTH_SHORT).show()
        }, containerColor = Color.DarkGray , modifier = Modifier.padding(bottom = 56.dp )){
            Icon(painter = painterResource(R.drawable.baseline_add_24), tint = Color.White, contentDescription = "add") }
        },floatingActionButtonPosition = FabPosition.End,

    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
            Text(text = "Locations Screen", fontSize = 24.sp)
        }
    }
}