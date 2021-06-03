package com.bluemap.overcom_blue.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.bluemap.overcom_blue.user.UserManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Deferred
import javax.inject.Inject


class LocationService @Inject constructor(
        @ApplicationContext private val context:Context,
) {
    val mLocation = MutableLiveData<com.bluemap.overcom_blue.model.Location>()
    private val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val availableLocationService = when {
        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ->  NETWORK
        else -> LOCATION_UNAVAILABLE
    }

    private val locationListener = object: LocationListener {
        override fun onLocationChanged(location: android.location.Location?) {
            if (location != null) {
                Log.i(TAG, "${location.latitude}, ${location.longitude}")
                val myLocation = com.bluemap.overcom_blue.model.Location(location.latitude,location.longitude)
                UserManager.currentLocation = myLocation
                mLocation.value = myLocation
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String?) {}
        override fun onProviderDisabled(provider: String?) {}
    }

    fun requestSingleUpdate(){
        checkPermission()
        when(availableLocationService){
            NETWORK -> locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, Looper.myLooper())
            else -> Log.i(TAG,"Cannot available location service")
        }
    }


    private fun checkPermission(){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return
    }

    companion object{
        private const val LOCATION_UNAVAILABLE = 1000
        private const val NETWORK = 1002
        private const val TAG = "LocationService"
    }
}