package com.abdok.atmosphere.ui.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abdok.atmosphere.R
import com.abdok.atmosphere.ui.theme.ColorTextPrimary
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TopView(
    city: String = "Zefta",
    country: String = "Egypt"
    ,code:String = "03d"
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.marker_lottie))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = Int.MAX_VALUE,
        speed = 1.0f,
        isPlaying = true
    )

    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 48.dp,)
    ) {

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            , verticalAlignment = Alignment.CenterVertically
            , horizontalArrangement = Arrangement.Start
        ){
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(48.dp)
            )
            Column {

                Text(
                    text = "$city",
                    fontWeight = FontWeight.SemiBold, fontSize = 22.sp
                    , modifier = Modifier.padding(start = 8.dp)
                    , color = ColorTextPrimary
                )
                Text(
                    text = country,
                    fontWeight = FontWeight.SemiBold, fontSize = 22.sp
                    , modifier = Modifier.padding(start = 8.dp)
                    , color = ColorTextPrimary
                )
            }
        }

//        Text(
//            text = "$city,",
//            fontWeight = FontWeight.SemiBold, fontSize = 30.sp
//        )
//
//        Text(
//            text = country,
//            fontWeight = FontWeight.SemiBold, fontSize = 30.sp
//        )

        /*
                Text(
                    text = code.getWeatherNotification(),
                    fontWeight = FontWeight.SemiBold, fontSize = 14.sp,
                    modifier = Modifier.padding(top = 32.dp).fillMaxWidth(),
                    textAlign = TextAlign.Center
                    )
        */





    }
}

