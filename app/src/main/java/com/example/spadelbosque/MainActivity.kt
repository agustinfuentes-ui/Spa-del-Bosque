package com.example.spadelbosque

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.spadelbosque.navigation.AppNavHost
import com.example.spadelbosque.ui.theme.SpaTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            SpaTheme {
                val windowSizeClass: WindowSizeClass = calculateWindowSizeClass(this)
                AppNavHost(windowSizeClass = windowSizeClass)
            }
        }
    }
}
