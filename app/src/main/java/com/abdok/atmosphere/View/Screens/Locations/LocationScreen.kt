package com.abdok.atmosphere.View.Screens.Locations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abdok.atmosphere.Data.Response
import com.abdok.atmosphere.R
import com.abdok.atmosphere.View.Screens.CurvedNavBar
import com.abdok.atmosphere.View.Screens.Locations.components.FavouriteLocationCard
import com.abdok.atmosphere.View.Screens.Locations.components.FavouriteLocationsView
import com.abdok.atmosphere.View.Screens.Locations.components.SwipeToDeleteContainer

@Composable
fun LocationsScreen(
    viewModel: LocationsViewModel,
    onLocationClick: () -> Unit
) {

    val favouriteLocations = viewModel.favLocations.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState()}

    LaunchedEffect(Unit) {
        viewModel.getFavouriteLocations()
    }

    LaunchedEffect(Unit) {
        viewModel.message.collect { message ->
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "Undo"
                , duration = SnackbarDuration.Short
            )
        }
    }

    CurvedNavBar.mutableNavBarState.value = true
    val context = LocalContext.current
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState ,
            modifier = Modifier.wrapContentHeight(align = Alignment.Top)) },
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
            }

            is Response.Loading -> {}

            is Response.Success -> {
                val locations = (favouriteLocations.value as Response.Success).data
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                ) {
                     FavouriteLocationsView(locations = locations){
                         viewModel.deleteFavouriteLocation(it)
                     }


                }
            }
        }
    }
}

