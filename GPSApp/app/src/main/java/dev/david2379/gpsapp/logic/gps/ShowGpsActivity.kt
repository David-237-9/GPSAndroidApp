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
import dev.david2379.gpsapp.logic.gps.model.LocationPermission
import dev.david2379.gpsapp.ui.theme.GPSAppTheme
import dev.david2379.gpsapp.ui.gps.ShowGpsScreen
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val GPS_REFRESH_RATE_MS = 500L

class ShowGpsActivity : ComponentActivity() {
    private var refreshGpsJob: Job? = null

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)

        LocationPermission.init(this) // Request location permission if not granted

        val gpsData = GPSLocationGetter(this)

        setContent {
            var gpsLocation by remember { mutableStateOf<GPSLocation?>(null) }
            var counter by remember { mutableStateOf(0) }

            fun startRefreshJob () {
                refreshGpsJob?.cancel()
                refreshGpsJob = lifecycleScope.launch { // Start a coroutine to refresh GPS data
                    val startTime = System.currentTimeMillis()
                    val waitTime = GPS_REFRESH_RATE_MS - (System.currentTimeMillis() - startTime)
//                    if (waitTime > 0) delay(waitTime)
                    delay(GPS_REFRESH_RATE_MS) // Ensure a fixed refresh rate
                    startRefreshJob()
                    gpsData.getLocation(
                        onResult = { newLocation ->
                            println("NEW LOCATION #${++counter} " + System.currentTimeMillis())
                            gpsLocation = newLocation
                        },
                        gpsLocation,
                    )
                }
            }
            println("STARTING GPS REFRESH " + System.currentTimeMillis())
            startRefreshJob()

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
