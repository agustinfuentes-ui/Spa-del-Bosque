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

@Composable
fun MainShell(
    navController: NavController,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navController = navController,
                currentRoute = currentRoute,
                onCloseDrawer = {scope.launch {drawerState.close()}}
            )
        }
    ) {
        Scaffold(
            topBar = {
                AppTopBar(
                    scope = scope,
                    drawerState = drawerState,
                    onCartClick = { navController.navigate(com.example.spadelbosque.navigation.Route.Carrito.path)  },
                    onProfileClick = { navController.navigate(com.example.spadelbosque.navigation.Route.Perfil.path) }
                )
            }
        ) { padding ->
            Box(Modifier.padding(padding))
             {
                content()
            }
        }
    }
}
