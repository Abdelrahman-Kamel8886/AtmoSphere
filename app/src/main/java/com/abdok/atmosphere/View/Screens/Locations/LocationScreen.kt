package com.abdok.atmosphere.View.Screens.Locations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abdok.atmosphere.Data.Models.FavouriteLocation
import com.abdok.atmosphere.Data.Models.FavouriteLocation.Companion.toJson
import com.abdok.atmosphere.Data.Response
import com.abdok.atmosphere.R
import com.abdok.atmosphere.Utils.CountryHelper
import com.abdok.atmosphere.View.Screens.CurvedNavBar
import com.abdok.atmosphere.View.Screens.Locations.Components.FavouriteLocationsView
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LocationsScreen(
    viewModel: LocationsViewModel,
    onLocationClick: () -> Unit,
    onItemSelected: (String) ->Unit
) {

    val context = LocalContext.current

    val favouriteLocations = viewModel.favLocations.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState()}

    val  scope = rememberCoroutineScope()

    //val message : MutableState<String?> = remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        viewModel.getFavouriteLocations()
    }

    CurvedNavBar.mutableNavBarState.value = true

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState , modifier = Modifier.wrapContentHeight(align = Alignment.Top)) },
        containerColor = Color.White,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onLocationClick()
            }, containerColor = Color.DarkGray, modifier = Modifier.padding(bottom = 56.dp)) {
                Icon(
                    painter = painterResource(R.drawable.baseline_add_24),
                    tint = Color.White,
                    contentDescription = "add"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,

        ) { innerPadding ->

        when (favouriteLocations.value) {
            is Response.Error -> {
                EmptyLocationsView(padding = innerPadding)
            }

            is Response.Loading -> {}

            is Response.Success -> {
                val locations = (favouriteLocations.value as Response.Success).data

                var list by remember { mutableStateOf(locations) }

                if(list.isNullOrEmpty()){
                    EmptyLocationsView(padding = innerPadding)
                }
                else{
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {

                        FavouriteLocationsView(
                            locations = list,
                            snackbarHostState = snackbarHostState,
                            onDelete = {
                                viewModel.deleteFavouriteLocation(it)
                                list -= it
                            },
                            onSelected = {
                                val value = it.toJson()
                                onItemSelected(value)
                            }
                        )
                    }
                }


            }
        }
    }
}

@Composable
fun EmptyLocationsView(padding : PaddingValues) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_locations))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = Int.MAX_VALUE,
        speed = 1.0f,
        isPlaying = true
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(300.dp)
            )
            Text(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.DarkGray,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                text = "It looks empty here. Save a location to get started!")
        }
    }
}