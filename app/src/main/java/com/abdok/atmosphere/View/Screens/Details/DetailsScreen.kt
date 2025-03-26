package com.abdok.atmosphere.View.Screens.Details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.abdok.atmosphere.Data.Models.FavouriteLocation
import com.abdok.atmosphere.View.Screens.CurvedNavBar

@Composable
fun DetailsScreen(favouriteLocation: FavouriteLocation) {

    CurvedNavBar.mutableNavBarState.value = false

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Text(modifier =Modifier.fillMaxWidth() , text =  "Details Screen for ${favouriteLocation.location}")
    }

}
