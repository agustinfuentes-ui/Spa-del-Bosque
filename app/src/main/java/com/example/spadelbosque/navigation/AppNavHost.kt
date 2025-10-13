package com.example.spadelbosque.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spadelbosque.ui.components.MainShell

@Composable
fun AppNavHost() {
    val nav = rememberNavController()
    NavHost(
        navController = nav,
        startDestination = Route.Home.path
    ) {
        composable(Route.Home.path) {
            MainShell(nav) {
                Text("HOME de prueba")
            }
        }
    }
}
