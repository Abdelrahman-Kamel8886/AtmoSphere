package com.abdok.atmosphere.utils

import android.location.Location
import androidx.compose.ui.graphics.Brush
import androidx.lifecycle.MutableLiveData
import com.abdok.atmosphere.enums.Speeds
import com.abdok.atmosphere.enums.Units

object SharedModel {

    var currentDegree = Units.getDegreeByValue(Units.METRIC.value)
    var currentSpeed = Speeds.getDegree(Speeds.METERS_PER_SECOND.degree)

    var screenBackground = MutableLiveData<Brush>()

    var currentLocation : Location? = null

}