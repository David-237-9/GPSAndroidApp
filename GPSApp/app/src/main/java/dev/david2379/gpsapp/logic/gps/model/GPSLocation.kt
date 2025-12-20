package dev.david2379.gpsapp.logic.gps.model

data class GPSLocation (
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double,
    val lastCalculatedSpeed: Float,
    val calculatedSpeedList: List<Float>,
    val averageSpeed: Float,
)
