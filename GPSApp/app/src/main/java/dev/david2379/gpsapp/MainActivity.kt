package dev.david2379.gpsapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.david2379.gpsapp.logic.gps.ShowGpsActivity
import dev.david2379.gpsapp.ui.MainScreen
import dev.david2379.gpsapp.ui.theme.GPSAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val navigateToGPSLocationIntent: Intent by lazy {
            Intent(this, ShowGpsActivity::class.java)
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GPSAppTheme {
                MainScreen(
                    onGPSLocationNavigate = { startActivity(navigateToGPSLocationIntent) },
                )
            }
        }
    }
}
