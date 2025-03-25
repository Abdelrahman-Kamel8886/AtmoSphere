package com.abdok.atmosphere.View.Screens.Map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abdok.atmosphere.BuildConfig
import com.abdok.atmosphere.Data.Response
import com.abdok.atmosphere.Enums.MapSelection
import com.abdok.atmosphere.R
import com.abdok.atmosphere.Utils.CountryHelper
import com.abdok.atmosphere.Utils.ViewHelpers.BackgroundMapper
import com.abdok.atmosphere.View.Screens.CurvedNavBar
import com.abdok.atmosphere.View.Screens.Home.DrawHome
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.LocationBias
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.kotlin.awaitFindAutocompletePredictions
import com.google.android.libraries.places.compose.autocomplete.components.PlacesAutocompleteTextField
import com.google.android.libraries.places.compose.autocomplete.models.AutocompletePlace
import com.google.android.libraries.places.compose.autocomplete.models.toPlaceDetails
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds


val coordinates = MutableStateFlow<LatLng?>(null)

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(viewModel: MapViewModel , onBackClick: () -> Unit) {

    CurvedNavBar.mutableNavBarState.value = false

    val cityLocationState = viewModel.addressLocation.collectAsStateWithLifecycle()

    val context = LocalContext.current

    Places.initializeWithNewPlacesApiEnabled(context, BuildConfig.MAP_API_KEY)
    val placesClient = Places.createClient(context)

    val bias: LocationBias = RectangularBounds.newInstance(
        LatLng(39.9, -105.5),
        LatLng(40.1, -105.0)
    )

    val searchTextFlow = MutableStateFlow("")
    val searchText = searchTextFlow.collectAsStateWithLifecycle()
    var predictions = remember { mutableStateOf(emptyList<AutocompletePrediction>()) }

    val locationMarkerState: MutableState<MarkerState?> = remember { mutableStateOf(null) }
    
    val snackbarHostState = remember { SnackbarHostState() }
    val insertError = viewModel.insertionState.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()




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
                modifier = Modifier.padding(16.dp),
                colors = TopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.Transparent,
                    navigationIconContentColor = Color.Black,
                    actionIconContentColor = Color.Black,
                    scrolledContainerColor = Color.White
                ),
                title = {
                    OutlinedTextField(
                        value = searchText.value,
                        onValueChange = {
                            searchTextFlow.value = it
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, shape = RoundedCornerShape(8.dp)),
                        placeholder = {
                            Text(
                                text = stringResource(R.string.search_with_city_name),
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        },
                        textStyle = TextStyle(color = Color.DarkGray, fontSize = 14.sp),
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
                        onDismissRequest = { predictions.value = emptyList() }
                    ) {
                        predictions.value.forEach { autocompletePlace ->
                            DropdownMenuItem(

                                text = {
                                    Text(
                                        text = autocompletePlace.getFullText(null).toString()
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
                                    viewModel.getCityLocation(
                                        autocompletePlace.getFullText(null).toString()
                                    )
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
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(map) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                onMapClick = {
                    locationMarkerState.value = MarkerState(position = it)
                    viewModel.getCityName(it)
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
                locationMarkerState.value?.let {
                    Marker(
                        state = it,
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
                            locationMarkerState.value =
                                MarkerState(position = LatLng(data.first.lat, data.first.lon))

                        }
                        AddressCard(
                            "${data.first.name} , ${CountryHelper.getCountryNameFromCode(data.first.country)}",
                            locationMarkerState.value!!.position,
                            viewModel
                        ){onBackClick()}
                    }
                }
            }

        }
        
    }
    
}


@Composable
fun AddressCard(address: String = "Zefta , Egypt", latLng: LatLng, viewModel: MapViewModel , onBackClick: () -> Unit) {

    val insertionState = viewModel.insertionState.collectAsStateWithLifecycle()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(64.dp)
            .background(color = Color.Gray, shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = address, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White
            )

            when (insertionState.value) {
                is Response.Loading -> {
                    CircularProgressIndicator(color = Color.White)
                }

                is Response.Success -> {
                    onBackClick()
                }

                else -> {
                    Button(onClick = {
                        viewModel.selectLocation(address, latLng)
                    }, modifier = Modifier.padding(top = 16.dp)) {
                        Text(text = "Select Location", color = Color.White)
                    }
                }
            }
        }

    }
}