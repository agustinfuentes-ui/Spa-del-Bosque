package com.example.spadelbosque

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.spadelbosque.navigation.AppNavHost
import com.example.spadelbosque.ui.theme.SpaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpaTheme {
                AppNavHost()
            }
        }
    }
}
