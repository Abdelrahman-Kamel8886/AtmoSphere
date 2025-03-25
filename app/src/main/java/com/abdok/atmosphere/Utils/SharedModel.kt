package com.abdok.atmosphere.Utils

import android.location.Location
import androidx.compose.ui.graphics.Brush
import androidx.lifecycle.MutableLiveData
import com.abdok.atmosphere.Enums.Units

object SharedModel {

    var currentDegree = Units.getDegreeByValue(Units.METRIC.value)

    var screenBackground = MutableLiveData<Brush>()

    var currentLocation : Location? = null

}