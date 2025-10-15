package com.example.spadelbosque.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spadelbosque.ui.components.MainShell
import com.example.spadelbosque.ui.screens.home.HomeScreen
import com.example.spadelbosque.ui.screens.servicios.ServiciosScreen
import com.example.spadelbosque.ui.screens.blogs.BlogScreen
import com.example.spadelbosque.ui.screens.nosotros.NosotrosScreen
import com.example.spadelbosque.ui.screens.contacto.ContactoScreen
import com.example.spadelbosque.ui.screens.carrito.CarritoScreen
import com.example.spadelbosque.ui.screens.perfil.PerfilScreen
//import com.example.spadelbosque.ui.screens.login.LoginScreen
//import com.example.spadelbosque.ui.screens.registro.RegistroScreen

// import androidx.navigation.NavType
// import androidx.navigation.navArgument
// import com.example.spadelbosque.ui.screens.servicios.ServicioDetalleScreen

@Composable
fun AppNavHost() {
    val nav = rememberNavController()
    NavHost(
        navController = nav,
        startDestination = Route.Home.path,
        route = "main"
    ) {
        composable(Route.Home.path)         { MainShell(nav) { HomeScreen() } }
        composable(Route.Servicios.path)    { MainShell(nav) { ServiciosScreen()} }
        composable(Route.Blogs.path)        { MainShell(nav) { BlogScreen() } }
        composable(Route.Nosotros.path )    { MainShell(nav) { NosotrosScreen() } }
        composable(Route.Contacto.path)     { MainShell(nav) { ContactoScreen() } }
        composable(Route.Carrito.path)      { MainShell(nav) { CarritoScreen() } }
        composable(Route.Perfil.path )      { MainShell(nav) { PerfilScreen() } }


        // Auth (si no los usarás ahora, déjalos comentados)
        // composable(Route.Login.path)     { LoginScreen() }
        // composable(Route.Registro.path)  { RegistroScreen() }

        // Detalle
        /*
        composable(
            route = Route.ServicioDetalle.path,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStack ->
            val id = backStack.arguments?.getString("id") ?: ""
            MainShell(nav) { ServicioDetalleScreen(id) }
        }
        */
    }
}
