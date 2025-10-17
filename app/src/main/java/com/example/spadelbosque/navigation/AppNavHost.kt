package com.example.spadelbosque.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.spadelbosque.ui.components.MainShell
import com.example.spadelbosque.ui.screens.blogs.BlogScreen
import com.example.spadelbosque.ui.screens.carrito.CarritoScreen
import com.example.spadelbosque.ui.screens.contacto.ContactoScreen
import com.example.spadelbosque.ui.screens.home.HomeScreen
import com.example.spadelbosque.ui.screens.login.LoginScreen
import com.example.spadelbosque.ui.screens.nosotros.NosotrosScreen
import com.example.spadelbosque.ui.screens.perfil.PerfilScreen
import com.example.spadelbosque.ui.screens.registro.RegistroScreen
import com.example.spadelbosque.ui.screens.servicios.ServicioDetalleScreen
import com.example.spadelbosque.ui.screens.servicios.ServiciosScreen
import com.example.spadelbosque.viewmodel.AuthViewModel
import com.example.spadelbosque.viewmodel.ContactoViewModel

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Route.Home.path
    ) {
        // --- Flujo de Autenticación ---
        composable(Route.Login.path) {
            LoginScreen(navController = navController, viewModel = authViewModel)
        }
        composable(Route.Registro.path) {
            RegistroScreen(navController = navController, viewModel = authViewModel)
        }

        // --- Flujo Principal (con MainShell) ---
        composable(Route.Home.path) {
            MainShell(navController) { HomeScreen() }
        }
        composable(Route.Servicios.path) {
            MainShell(navController) { ServiciosScreen(navController = navController) }
        }
        composable(Route.Blogs.path) {
            MainShell(navController) { BlogScreen() }
        }
        composable(Route.Nosotros.path) {
            MainShell(navController) { NosotrosScreen() }
        }
        composable(Route.Contacto.path) {
            val contactoViewModel: ContactoViewModel = viewModel()
            MainShell(navController) { ContactoScreen(navController, contactoViewModel) }
        }
        composable(Route.Carrito.path) {
            MainShell(navController) { CarritoScreen() }
        }
        composable(Route.Perfil.path) {
            MainShell(navController) { PerfilScreen() }
        }
        composable(
            route = "servicio_detalle/{sku}",
            arguments = listOf(navArgument("sku") { type = NavType.StringType })
        ) { backStackEntry ->
            val sku = backStackEntry.arguments?.getString("sku") ?: ""
            MainShell(navController) {
                ServicioDetalleScreen(navController = navController, servicioSku = sku)
            }
        }
    }
}
