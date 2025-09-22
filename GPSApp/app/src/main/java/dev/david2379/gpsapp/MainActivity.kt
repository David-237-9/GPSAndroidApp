package dev.david2379.gpsapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
