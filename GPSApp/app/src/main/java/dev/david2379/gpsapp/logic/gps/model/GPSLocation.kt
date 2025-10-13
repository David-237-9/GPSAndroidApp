package dev.david2379.gpsapp.logic.gps.model

data class GPSLocation (
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double,
    val locationSpeed: Float,
    val locationManuallyCalculatedSpeed: Float,
)
