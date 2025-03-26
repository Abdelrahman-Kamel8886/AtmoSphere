package com.abdok.atmosphere.View.Screens.Locations.Components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.abdok.atmosphere.Data.Models.FavouriteLocation
import com.abdok.atmosphere.R


@Composable
fun FavouriteLocationsView(
    locations: List<FavouriteLocation>,
    snackbarHostState: SnackbarHostState,
    onDelete: (FavouriteLocation) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                tint = Color.DarkGray,
                contentDescription = null
            )
            Text(
                text = stringResource(R.string.saved_locations),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(locations , key = {it.name}){
                    SwipeToDeleteContainer(
                        item = it,
                        onDelete = { onDelete(it) },
                        snackbarHostState = snackbarHostState
                    ) {
                        FavouriteLocationCard(item = it)
                    }
            }
        }

        Spacer(modifier = Modifier.height(150.dp))
    }
}
