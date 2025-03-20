package com.abdok.atmosphere.Utils

import android.location.Location
import androidx.compose.ui.graphics.Brush
import androidx.lifecycle.MutableLiveData

object SharedModel {

    var screenBackground = MutableLiveData<Brush>()

    var currentLocation : Location? = null

}