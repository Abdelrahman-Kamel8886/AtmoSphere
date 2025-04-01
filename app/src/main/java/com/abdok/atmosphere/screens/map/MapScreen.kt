package com.abdok.atmosphere.screens.map

import android.annotation.SuppressLint
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abdok.atmosphere.BuildConfig
import com.abdok.atmosphere.data.models.Response
import com.abdok.atmosphere.enums.MapSelection
import com.abdok.atmosphere.R
import com.abdok.atmosphere.ui.components.CurvedNavBar
import com.abdok.atmosphere.screens.map.components.AddressCard
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.LocationBias
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.kotlin.awaitFindAutocompletePredictions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(viewModel: MapViewModel , mapSelection: MapSelection , onBackClick: () -> Unit) {

    LaunchedEffect(Unit) {
        CurvedNavBar.mutableNavBarState.emit(false)
    }

    val cityLocationState = viewModel.addressLocation.collectAsStateWithLifecycle()




    val context = LocalContext.current

    Places.initializeWithNewPlacesApiEnabled(context, BuildConfig.MAP_API_KEY)
    val placesClient = Places.createClient(context)

    val geocoder = Geocoder(context, Locale.getDefault())


    val bias: LocationBias = RectangularBounds.newInstance(
        LatLng(39.9, -105.5),
        LatLng(40.1, -105.0)
    )

    val searchTextFlow = MutableStateFlow("")
    val searchText = searchTextFlow.collectAsStateWithLifecycle()
    var predictions = remember { mutableStateOf(emptyList<AutocompletePrediction>()) }

    val locationMarkerState = rememberMarkerState(position = LatLng(0.0, 0.0))

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(40.0, 10.0), 2f)
    }


    val snackbarHostState = remember { SnackbarHostState() }
    val insertError = viewModel.insertionState.collectAsStateWithLifecycle()


    LaunchedEffect(locationMarkerState.position) {
        if (locationMarkerState.position != LatLng(0.0, 0.0)) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(locationMarkerState.position , 8f),
                durationMs = 1000
            )
        }
    }


    LaunchedEffect(searchTextFlow) {
        searchTextFlow.debounce(500.milliseconds).collect { query: String ->
            val response = placesClient.awaitFindAutocompletePredictions {
                locationBias = bias
                typesFilter = listOf(PlaceTypes.CITIES)
                this.query = query
            }
            predictions.value = response.autocompletePredictions
        }
    }

    LaunchedEffect(Unit) {
        viewModel.message.collect { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        snackbarHost = {SnackbarHost(snackbarHostState)}
        ,topBar = {
            TopAppBar(
                colors = TopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.Transparent,
                    navigationIconContentColor = Color.Black,
                    actionIconContentColor = Color.Black,
                    scrolledContainerColor = Color.White),
                title = {
                    OutlinedTextField(value = searchText.value, onValueChange = {
                        searchTextFlow.value = it
                        },
                        singleLine = true,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .fillMaxWidth()
                            .background(Color.White, shape = RoundedCornerShape(50.dp))
                        ,
                        placeholder = {
                            Text(
                                text = stringResource(R.string.search_with_city_name),
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        },

                        shape = RoundedCornerShape(50.dp)
                        ,

                        textStyle = TextStyle(color = Color.DarkGray, fontSize = 14.sp),
                        leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Search",
                                    tint = Color.DarkGray
                                )

                        },
                        trailingIcon = {
                            if (searchText.value.isNotEmpty()) {
                                IconButton(onClick = { searchTextFlow.value = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear"
                                    )
                                }
                            }
                        }
                    )

                    DropdownMenu(
                        expanded = predictions.value.isNotEmpty(),
                        onDismissRequest = { predictions.value = emptyList()
                        }
                    ) {
                        predictions.value.forEach { autocompletePlace ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = autocompletePlace.getFullText(null).toString(),
                                    )
                                },
                                onClick = {
                                    Log.i(
                                        "TAG",
                                        "MapScreen 1 : ${
                                            autocompletePlace.getFullText(null).toString()
                                        }"
                                    )
                                    Log.i(
                                        "TAG",
                                        "MapScreen 2 : ${autocompletePlace.getPrimaryText(null)}"
                                    )

                                    Log.i(
                                        "TAG",
                                        "MapScreen 2 : ${autocompletePlace.getFullText(null)}"
                                    )


                                    viewModel.getLatLonByPlaceId(placesClient, autocompletePlace)
                                    searchTextFlow.value = ""
                                    predictions.value = emptyList()
                                }
                            )
                        }
                    }

                }
            )
        }
    ) { paddingValues ->

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {

            val (map, card) = createRefs()

            GoogleMap(
                cameraPositionState = cameraPositionState,
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(map) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                onMapClick = {
                    locationMarkerState.position = it
                    viewModel.getCityNameFromLatLng(geocoder,it)
                },
                properties = MapProperties(
                    isMyLocationEnabled = false,
                    mapType = MapType.HYBRID
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    compassEnabled = false,
                    mapToolbarEnabled = false
                )
            ) {
                if (locationMarkerState.position != LatLng(0.0, 0.0)) {
                    Marker(
                        state = locationMarkerState,
                        /*  title = "New Location",
                      snippet = "Updated Marker Position"*/
                    )
                }

            }

            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, top = 360.dp)
                .constrainAs(card) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ) {
                when (cityLocationState.value) {
                    is Response.Loading -> {
                    }

                    is Response.Error -> {
                        val message = (cityLocationState.value as Response.Error).exception
                        Text(text = message)
                    }

                    is Response.Success -> {
                        val data = (cityLocationState.value as Response.Success).data
                        if (data.second) {
                            locationMarkerState.position = LatLng(data.first.lat, data.first.lon)

                        }
                        AddressCard(
                            "${data.first.name}",
                            locationMarkerState.position,
                            viewModel
                            , mapSelection
                        ){onBackClick()}
                    }
                }
            }

        }

    }
    
}

