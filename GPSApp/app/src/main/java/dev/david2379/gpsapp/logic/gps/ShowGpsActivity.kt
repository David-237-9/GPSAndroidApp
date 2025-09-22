package dev.david2379.gpsapp.logic.gps

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import dev.david2379.gpsapp.logic.gps.model.GPSLocation
import dev.david2379.gpsapp.logic.gps.model.GPSLocationGetter
import dev.david2379.gpsapp.ui.theme.GPSAppTheme
import dev.david2379.gpsapp.ui.gps.ShowGpsScreen
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ShowGpsActivity : ComponentActivity() {
    private var refreshGpsJob: Job? = null

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)

        val gpsData = GPSLocationGetter(this)

        setContent {
            var gpsLocation by remember { mutableStateOf<GPSLocation?>(null) }

            fun startRefreshGpsJob() {
                refreshGpsJob = lifecycleScope.launch {
                    while (true) {
                        println("Refreshing GPS Location...")
                        gpsData.getLocation(onResult = { newLocation ->
                            println("Got new GPS Location: $newLocation")
                            gpsLocation = newLocation
                        })
                        delay(2000) // Refresh rate
                    }
                }
            }
            startRefreshGpsJob()

            GPSAppTheme {
                ShowGpsScreen(gpsLocation)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        refreshGpsJob?.cancel()
    }
}
