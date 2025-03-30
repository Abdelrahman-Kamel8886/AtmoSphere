package com.abdok.atmosphere.ui.Screens.Map.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abdok.atmosphere.Data.Response
import com.abdok.atmosphere.Enums.MapSelection
import com.abdok.atmosphere.R
import com.abdok.atmosphere.ui.Screens.Map.MapViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.android.gms.maps.model.LatLng

@Composable
fun AddressCard(address: String = "Zefta , Egypt", latLng: LatLng, viewModel: MapViewModel
                , mapSelection: MapSelection, onBackClick: () -> Unit) {

    val insertionState = viewModel.insertionState.collectAsStateWithLifecycle()

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.marker_lottie))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = Int.MAX_VALUE,
        speed = 1.0f,
        isPlaying = true
    )


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 64.dp , start = 16.dp , end = 32.dp)
            .background(color = Color.White, shape = RoundedCornerShape(12.dp))
        ,        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = address, fontWeight = FontWeight.Bold,
                fontSize = 20.sp, color = Color.DarkGray
                , modifier = Modifier.padding(top = 8.dp , bottom = 8.dp)
            )

            when (insertionState.value) {
                is Response.Loading -> {
                    CircularProgressIndicator(
                        color = Color.Red, modifier = Modifier.padding(16.dp)
                    )
                }

                is Response.Success -> {
                    onBackClick()
                }

                else -> {
                    Button(onClick = {
                        viewModel.selectLocation(address.substringBefore(","), latLng , mapSelection)
                    }, modifier = Modifier.padding(top = 8.dp , bottom = 16.dp) ,
                        colors = ButtonDefaults.buttonColors(Color(0xFF43A047))) {
                        Text(text = "Select Location", color = Color.White)
                    }
                }
            }
        }

    }
}