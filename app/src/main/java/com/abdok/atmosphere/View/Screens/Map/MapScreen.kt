package com.abdok.atmosphere.View.Screens.Map

import androidx.compose.foundation.background
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState


@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen() {
    val defaultBackground = Color(0xFFF5F5F5)

    val locationMarkerState: MutableState<MarkerState?> = remember { mutableStateOf(null) }
    val searchQuery = remember { mutableStateOf("") }

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
                    )
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
                zoomControlsEnabled = false, // Removes + and - icons
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
    }


}
