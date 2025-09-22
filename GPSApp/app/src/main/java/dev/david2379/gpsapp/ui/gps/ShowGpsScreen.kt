package dev.david2379.gpsapp.ui.gps

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.david2379.gpsapp.logic.gps.model.GPSLocation

@Composable
fun ShowGpsScreen(gpsData: GPSLocation?) {
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text("Latitude: ${gpsData?.latitude}")
            Text("Longitude: ${gpsData?.longitude}")
        }
    }
}
