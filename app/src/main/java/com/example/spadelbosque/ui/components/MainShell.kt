package com.example.spadelbosque.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.spadelbosque.navigation.Route
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

@Composable
fun MainShell(
    navController: NavController,
    windowSizeClass: WindowSizeClass,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val useRail = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> false
        WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> true
        else -> false
    }

        ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navController = navController,
                currentRoute = currentRoute,
                onCloseDrawer = {scope.launch {drawerState.close()}}
            )
        }
    ){
            Scaffold(
                topBar = {
                    AppTopBar(
                        scope = scope,
                        drawerState = drawerState,
                        onCartClick = { navController.navigate(Route.Carrito.path) },
                        onProfileClick = { navController.navigate(Route.Perfil.path) }
                    )
                }
            ) { padding ->
                Row(Modifier.padding(padding)) {
                    if (useRail) {
                        RailContent(navController = navController, currentRoute = currentRoute)
                    }
                    Box(Modifier.fillMaxSize()) {
                        content()
                    }
                }
            }
        }
}