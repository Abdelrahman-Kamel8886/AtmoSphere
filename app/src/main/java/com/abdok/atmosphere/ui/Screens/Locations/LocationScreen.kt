package com.abdok.atmosphere.ui.Screens.Locations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abdok.atmosphere.data.models.FavouriteLocation.Companion.toJson
import com.abdok.atmosphere.data.Response
import com.abdok.atmosphere.R
import com.abdok.atmosphere.ui.CurvedNavBar
import com.abdok.atmosphere.ui.Screens.Locations.Components.EmptyLocationsView
import com.abdok.atmosphere.ui.Screens.Locations.Components.FavouriteLocationsView

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
            }, containerColor = Color.DarkGray, modifier = Modifier.padding(bottom = 56.dp)
                , shape = RoundedCornerShape(100.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_add_location_alt_24),
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

