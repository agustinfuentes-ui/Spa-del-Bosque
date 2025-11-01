package com.example.spadelbosque

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass

import com.example.spadelbosque.ui.theme.SpaTheme
import com.example.spadelbosque.navigation.AppNavHost

import com.example.spadelbosque.data.local.AppDatabase
import com.example.spadelbosque.repository.AuthRepositoryImpl
import com.example.spadelbosque.viewmodel.AuthViewModel
import com.example.spadelbosque.viewmodel.factory.AuthVmFactory

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
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
