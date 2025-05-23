package com.abdok.atmosphere.workers

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.abdok.atmosphere.data.models.Response
import com.abdok.atmosphere.utils.SharedModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationWorker(context: Context,
                     workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    companion object{
        val mutableLiveLocation : MutableLiveData<Response<Location>> = MutableLiveData(Response.Loading)
    }

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private lateinit var locationCallback: LocationCallback

    override suspend fun doWork(): Result {
        SharedModel.ll = true
        return try {
            if (isNetworkAvailable()) {
                getLocation()
                Result.success()
            }
            else{
                mutableLiveLocation.postValue(Response.Error("No Internet Connection"))
                Result.failure()
            }

        } catch (e: Exception) {
            mutableLiveLocation.postValue(Response.Error("Failed to get location: ${e.message}"))
            Log.e("LocationWorker", "Failed to get location: ${e.message}")
            Result.retry()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 0).build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                Log.i("TAG", "work onLocationResult: $locationResult")
                mutableLiveLocation.postValue(Response.Success(locationResult.lastLocation!!))
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }
        fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper())
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? android.net.ConnectivityManager
        return connectivityManager?.activeNetwork != null
    }

}