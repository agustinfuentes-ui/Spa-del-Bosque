package com.example.spadelbosque.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.spadelbosque.navigation.Route

@Composable
fun DrawerContent(
    navController: NavController
) {
    ModalDrawerSheet {
        NavigationDrawerItem(
            label = { Text("Home") },
            selected = false,
            onClick = { navController.navigate(Route.Home.path) }
        )
    }
}
