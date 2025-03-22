package com.abdok.atmosphere.View.Screens.Map

import android.content.Context
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abdok.atmosphere.BuildConfig
import com.abdok.atmosphere.Data.Response
import com.abdok.atmosphere.R
import com.abdok.atmosphere.View.Screens.CurvedNavBar
import com.abdok.atmosphere.View.Screens.Home.DrawHome
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
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds


val coordinates = MutableStateFlow<LatLng?>(null)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(viewModel: MapViewModel) {

    CurvedNavBar.mutableNavBarState.value = false

    val cityLocationState = viewModel.addressLocation.collectAsStateWithLifecycle()

    val context = LocalContext.current

    Places.initializeWithNewPlacesApiEnabled(context, BuildConfig.MAP_API_KEY)
    val placesClient = Places.createClient(context)

    val bias: LocationBias = RectangularBounds.newInstance(
        LatLng(39.9, -105.5), // SW lat, lng
        LatLng(40.1, -105.0) // NE lat, lng
    )

    val searchTextFlow = MutableStateFlow("")
    val searchText = searchTextFlow.collectAsStateWithLifecycle()
    var predictions = remember { mutableStateOf(emptyList<AutocompletePrediction>())}

    val locationMarkerState: MutableState<MarkerState?> = remember { mutableStateOf(null) }
//    val searchQuery = remember { mutableStateOf("")}


    LaunchedEffect(Unit) {
        searchTextFlow.debounce(500.milliseconds).collect { query : String ->
            val response = placesClient.awaitFindAutocompletePredictions {
                locationBias = bias
                typesFilter = listOf(PlaceTypes.CITIES)
                this.query = query
            }
            predictions.value = response.autocompletePredictions
        }
    }


    Scaffold(
        topBar = {
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
                            .background(Color.White, shape = RoundedCornerShape(12.dp))
                        ,
                        placeholder = { Text(text = stringResource(R.string.search_with_city_name), color = Color.Gray , fontSize = 12.sp) },
                        textStyle = TextStyle(color = Color.DarkGray , fontSize = 14.sp),
                        trailingIcon = {
                            if (searchText.value.isNotEmpty()) {
                                IconButton(onClick = { searchTextFlow.value = "" }) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                                }
                            }
                        }
                    )

                    val coState = coordinates.collectAsStateWithLifecycle()


                    DropdownMenu(
                        expanded = predictions.value.isNotEmpty(),
                        onDismissRequest = { predictions.value = emptyList() }
                    ) {
                        predictions.value.forEach { autocompletePlace ->
                            DropdownMenuItem(

                                text = { Text(text = autocompletePlace.getFullText(null).toString()) },
                                onClick = {
                                    Log.i("TAG", "MapScreen 1 : ${autocompletePlace.getFullText(null).toString()}")
                                    Log.i("TAG", "MapScreen 2 : ${autocompletePlace.getPrimaryText(null)}")
                                    viewModel.getCityLocation(autocompletePlace.getFullText(null).toString())
                                    searchTextFlow.value = ""
                                    Toast.makeText(
                                        context,
                                        "Selected: ${autocompletePlace}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    predictions.value = emptyList()
                                }
                            )
                        }
                    }

                }
            )
        }
    ) { paddingValues ->

        GoogleMap(
            modifier = Modifier
                .fillMaxSize(),
            onMapClick = {
                locationMarkerState.value = MarkerState(position = it)
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
            .padding(16.dp)){
            when(cityLocationState.value){
                is Response.Loading -> {
                        CircularProgressIndicator()
                }
                is Response.Error -> {
                    val message = (cityLocationState.value as Response.Error).exception
                    Text(text = message)
                }
                is Response.Success -> {
                    val data = (cityLocationState.value as Response.Success).data
                    Text(text = data.state)
                }
            }
        }



        /*PlacesAutocompleteTextField(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 60.dp)
                .background(Color.Transparent),
            searchText = searchText.value,
            predictions = predictions.value.map { it.toPlaceDetails() },
            onQueryChanged = { searchTextFlow.value = it },
            onSelected = { autocompletePlace : AutocompletePlace ->
                // Handle the selected place
                Toast.makeText(
                    context,
                    "Selected: ${autocompletePlace.latLng?.latitude}",
                    Toast.LENGTH_SHORT
                ).show()

                predictions.value = emptyList()
                searchTextFlow.value = ""


            }
        )*/


    }


  /*  OutlinedTextField(
        value = searchQuery.value,
        onValueChange = { searchQuery.value = it },
        placeholder = { Text("Search with City or Country Name" , color = Color.Gray , fontSize = 12.sp) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        trailingIcon = {
            if (searchQuery.value.isNotEmpty()) {
                IconButton(onClick = { searchQuery.value = "" }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear Search"
                    )
                }
            }
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(12.dp))
    )*/

}