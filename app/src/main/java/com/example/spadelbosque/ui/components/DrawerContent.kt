package com.example.spadelbosque.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.spadelbosque.navigation.Route
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem

private fun drawerItems(): List<Triple<String, ImageVector, String>> = listOf(
    Triple("Home", Icons.Filled.Home, Route.Home.path),
    Triple("Servicios", Icons.Filled.Spa, Route.Servicios.path),
    Triple("Blogs", Icons.Filled.Newspaper, Route.Blogs.path),
    Triple("Nosotros", Icons.Filled.Info, Route.Nosotros.path),
    Triple("Contacto", Icons.Filled.Phone, Route.Contacto.path),
    // Si quieres Perfil en el rail, agrégalo aquí:
    // Triple("Perfil", Icons.Filled.Person, Route.Perfil.path),
)
@Composable
fun DrawerContent(
    navController: NavController,
    currentRoute: String?,
    onCloseDrawer: () -> Unit
) {
    val items = drawerItems()

    ModalDrawerSheet {
        Text(
            text = "SPA del Bosque",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
        items.forEach { (label, icon, route) ->
            NavigationDrawerItem(
                icon = {Icon(icon, contentDescription = label)},
                label = {Text(label,color = MaterialTheme.colorScheme.onSurface)},
                selected = currentRoute == route,
                onClick = {
                    navController.navigate(route){
                        popUpTo("main"){saveState = true}
                        launchSingleTop = true
                        restoreState = true
                    }
                    onCloseDrawer()
                },
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}

@Composable
fun RailContent(
    navController: NavController,
    currentRoute: String?
) {
    NavigationRail {
        drawerItems().forEach { (label, icon, route) ->
            NavigationRailItem(
                selected = currentRoute == route,
                onClick = {
                    navController.navigate(route) {
                        popUpTo("main") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) }
            )
        }
    }
}
