package com.example.spadelbosque.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.example.spadelbosque.ui.screens.carrito.CompraScreen
import com.example.spadelbosque.model.ItemCarrito
import com.example.spadelbosque.viewmodel.AuthViewModel
import com.example.spadelbosque.viewmodel.ContactoViewModel
import com.example.spadelbosque.viewmodel.CarritoViewModel
import com.example.spadelbosque.di.AppGraph
import com.example.spadelbosque.viewmodel.factory.CarritoVmFactory

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
            MainShell(navController) { HomeScreen(navController = navController) }
        }

        composable(Route.Servicios.path) {
            // ViewModel con factory (usa repo único de AppGraph)
            val carritoVm: CarritoViewModel =
                viewModel(factory = CarritoVmFactory(AppGraph.cartRepo))

            MainShell(navController) {
                ServiciosScreen(
                    navController = navController,
                    onAgregar = { sku: String, nombre: String, precio: Int ->
                        carritoVm.add(ItemCarrito(sku = sku, nombre = nombre, precio = precio))
                    }
                )
            }
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
        composable(Route.Perfil.path) {
            MainShell(navController) { PerfilScreen() }
        }
        composable(
            route = "servicio_detalle/{sku}",
            arguments = listOf(navArgument("sku") { type = NavType.StringType })
        ) { backStackEntry ->
            val sku = backStackEntry.arguments?.getString("sku") ?: ""
            val carritoVm: CarritoViewModel =
                viewModel(factory = CarritoVmFactory(AppGraph.cartRepo))
            MainShell(navController) {
                ServicioDetalleScreen(navController = navController,
                    servicioSku = sku,
                    onAgregar = { sku: String, nombre: String, precio: Int ->
                        carritoVm.add(ItemCarrito(sku = sku, nombre = nombre, precio = precio))
                    })
            }
        }
        composable(Route.Carrito.path) {
            val carritoVm: CarritoViewModel =  viewModel(factory = CarritoVmFactory(AppGraph.cartRepo))
            MainShell(navController)
            {
                CarritoScreen(
                    vm = carritoVm,
                    onSeguirAgregando = { navController.navigate(Route.Servicios.path) },
                    onComprar = {

                        navController.navigate(Route.Compra.path) {
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
        composable(Route.Compra.path) {
            val carritoVm: CarritoViewModel =
                viewModel(factory = CarritoVmFactory(AppGraph.cartRepo))
            MainShell(navController) {
                CompraScreen(
                    carritoVm = carritoVm,
                    onCancelar = {
                        navController.popBackStack()
                    },
                    onFinalizar = {
                        navController.navigate(Route.Home.path) {
                            popUpTo(Route.Home.path) { inclusive = true }
                            launchSingleTop = true
                        }
                        navController.getBackStackEntry(Route.Home.path)
                            .savedStateHandle["clear_cart"] = true
                    }
                )
            }
        }
    }

}
