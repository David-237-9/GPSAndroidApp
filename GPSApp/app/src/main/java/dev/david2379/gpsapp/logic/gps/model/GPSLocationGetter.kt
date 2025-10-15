package dev.david2379.gpsapp.logic.gps.model

import android.Manifest
import android.app.Activity
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class GPSLocationGetter(activity: Activity) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun getLocation(onResult: (GPSLocation?) -> Unit, lastLocation: GPSLocation?) {
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        ).addOnSuccessListener { successLocation ->
            if (successLocation != null) {
                val currentTime = System.currentTimeMillis()
                val speedFromLocation = successLocation.speed
                val calculatedSpeed = if (lastLocation != null) meterSecondToKilometerHour(
                    calculateSpeed(
                        manuallyCalcLocationsDistance(
                            successLocation.latitude,
                            successLocation.longitude,
                            lastLocation.latitude,
                            lastLocation.longitude,
                        ),
                        currentTime - lastLocation.timestamp
                    )
                ) else 0f
                var counter1 = 0f
                var counter2 = 0f
                lastLocation?.locationSpeedList?.forEach { counter1 += it }
                lastLocation?.locationManuallyCalculatedSpeedList?.forEach { counter2 += it }
                val average1 = (counter1 + speedFromLocation) / if (counter1 == 0f) 1 else lastLocation!!.locationSpeedList.size
                val average2 = (counter2 + calculatedSpeed) / if (counter2 == 0f) 1 else lastLocation!!.locationManuallyCalculatedSpeedList.size
                onResult(
                    GPSLocation(
                        currentTime,
                        successLocation.latitude,
                        successLocation.longitude,
                        speedFromLocation,
                        calculatedSpeed,
                        lastLocation?.locationSpeedList?.let {
                            if (it.size >= 100) it.drop(1) + successLocation.speed
                            else it + successLocation.speed
                        } ?: listOf(speedFromLocation),
                        lastLocation?.locationManuallyCalculatedSpeedList?.let {
                            if (it.size >= 100) it.drop(1) + calculatedSpeed
                            else it + calculatedSpeed
                        } ?: listOf(calculatedSpeed),
                        average1,
                        average2,
                    )
                )
            } else {
                onResult(null)
            }
        }.addOnFailureListener { exception ->
            onResult(null)
        }
    }

    /**
     * Calculate speed in meters/second
     */
    private fun calculateSpeed(distanceMeters: Float, timeMillis: Long): Float {
        if (timeMillis == 0L) return 0f
        return (distanceMeters / (timeMillis / 1000f))
    }

    private fun meterSecondToKilometerHour(speed: Float): Float = speed * 3.6f

    private fun manuallyCalcLocationsDistance(
        latitude1: Double,
        longitude1: Double,
        latitude2: Double,
        longitude2: Double,
    ): Float {
        val earthRadius = 6371000.0 // meters
        val dLat = Math.toRadians(latitude2 - latitude1)
        val dLon = Math.toRadians(longitude2 - longitude1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(latitude1)) * cos(Math.toRadians(latitude2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return (earthRadius * c).toFloat()
    }
}
