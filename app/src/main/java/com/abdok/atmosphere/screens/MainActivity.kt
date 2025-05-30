package com.abdok.atmosphere.screens

import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.abdok.atmosphere.R
import com.abdok.atmosphere.data.local.LocalDataSourceImpl
import com.abdok.atmosphere.data.local.room.LocalDataBase
import com.abdok.atmosphere.data.local.sharedPreference.SharedPreferencesImpl
import com.abdok.atmosphere.data.models.Response
import com.abdok.atmosphere.data.remote.RemoteDataSourceImpl
import com.abdok.atmosphere.data.remote.retrofit.RetroConnection
import com.abdok.atmosphere.data.repository.RepositoryImpl
import com.abdok.atmosphere.enums.Locations
import com.abdok.atmosphere.navigation.SetupNavHost
import com.abdok.atmosphere.ui.components.CurvedNavBar
import com.abdok.atmosphere.utils.SharedModel
import com.abdok.atmosphere.utils.network.NetworkStateObserver
import com.abdok.atmosphere.workers.LocationWorker
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest


class MainActivity : ComponentActivity() {

    private val REQUEST_LOCATION_CODE = 8886

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CurvedNavBar.activeIndex.value = 0

        val factory = MainViewModelFactory(
            RepositoryImpl.getInstance(
                RemoteDataSourceImpl.getInstance(
                    RetroConnection.retroServices
                ),
                LocalDataSourceImpl.getInstance(
                    LocalDataBase.getInstance().localDao(), SharedPreferencesImpl.getInstance()
                )
            )
        )
        viewModel = ViewModelProvider(this , factory)[MainViewModel::class.java]
        viewModel.applyLanguage(context = this)
        enableEdgeToEdge()
        setContent {

            val locationTypeState = viewModel.locationState.observeAsState()

            when (locationTypeState.value){
                Locations.Gps.value -> {

                    if (checkLocationPermission()) {
                        if (isLocationEnabled()) {
                            scheduleLocationWorker()
                        } else {
                            enableLocation()
                        }
                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.ACCESS_FINE_LOCATION
                            ), REQUEST_LOCATION_CODE
                        )
                    }

                    var locationState = LocationWorker.mutableLiveLocation.observeAsState()
                    when (locationState.value) {
                        is Response.Error -> {
                            /*val message = (locationState.value as Response.Error).exception
                            Box (Modifier.fillMaxSize() , contentAlignment = Alignment.Center){
                                Text(text = message)
                            }*/
                            MainScreen(null)
                        }

                        Response.Loading -> {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }

                        is Response.Success -> {
                            val location = (locationState.value as Response.Success).data
                            MainScreen(location)
                        }

                        null -> {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(text = "Null")
                            }

                        }
                    }
                }
                Locations.Map.value -> {
                    MainScreen(location = null)
                }
            }
        }
    }

    private fun checkLocationPermission(): Boolean {
        return checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun enableLocation() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { response ->
            scheduleLocationWorker()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(this, 123)
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scheduleLocationWorker()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


    fun scheduleLocationWorker(context: Context = applicationContext) {
        if (!SharedModel.ll) {
            val workRequest = OneTimeWorkRequestBuilder<LocationWorker>()
                .setConstraints(Constraints.Builder().build())
                .build()

            val workManager = WorkManager.getInstance(context)
            workManager.enqueue(workRequest)
        }
    }


    @Composable
    fun MainScreen(location: Location?) {

        val networkStateObserver = remember { NetworkStateObserver(this@MainActivity) }
        val isConnected by networkStateObserver.isConnected.observeAsState(true)

        val defaultBackground = Brush.linearGradient(
            listOf(Color(0xFFF5F5F5), Color(0xFFFFFFFF))
        )
        val navController = rememberNavController()
        val isNavBarVisible = CurvedNavBar.mutableNavBarState.collectAsStateWithLifecycle()
        Scaffold(
            bottomBar = {
                when (isNavBarVisible.value) {

                    true -> {
                        Log.e("TAG", "nav bar visible: " )
                        CurvedNavBar.ShowCurvedNavBar(navController)
                    }
                    false -> {
                        Log.e("TAG", "nav bar not visible: " )
                    }
                    null -> {
                        Log.e("TAG", "nav bar null: " )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .background(defaultBackground)
            ) {

                SetupNavHost(navController, location)
                if (!isConnected) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp)
                    ) {

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Gray, shape = RoundedCornerShape(4.dp))
                                .padding(vertical = 8.dp),
                            text = stringResource(R.string.offline_mood),
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }


}
