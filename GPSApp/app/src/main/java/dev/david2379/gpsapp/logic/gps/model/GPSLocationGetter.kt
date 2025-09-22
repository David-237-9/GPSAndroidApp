package dev.david2379.gpsapp.logic.gps.model

import android.Manifest
import android.app.Activity
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class GPSLocationGetter(activity: Activity) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun getLocation(onResult: (GPSLocation?) -> Unit) {
        println("GPSLocationGetter: Getting location...")
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        ).addOnSuccessListener { successLocation ->
            if (successLocation != null) {
                println("GPSLocationGetter: Got location: $successLocation")
                onResult(GPSLocation(successLocation.latitude, successLocation.longitude))
            } else {
                println("GPSLocationGetter: Location is null")
                onResult(null)
            }
        }.addOnFailureListener { exception ->
            println("GPSLocationGetter: Failed to get location: $exception")
            onResult(null)
        }
    }

}
