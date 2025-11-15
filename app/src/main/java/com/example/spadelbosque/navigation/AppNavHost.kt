package com.example.spadelbosque.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.spadelbosque.viewmodel.PerfilViewModel
import com.example.spadelbosque.viewmodel.factory.PerfilVmFactory
import com.example.spadelbosque.di.AppGraph
import com.example.spadelbosque.viewmodel.factory.CarritoVmFactory
import com.example.spadelbosque.viewmodel.factory.AuthVmFactory
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import com.example.spadelbosque.ui.screens.SplashScreen

@ExperimentalMaterial3Api
@Composable
fun AppNavHost(windowSizeClass: WindowSizeClass) {
    val navController = rememberNavController()

    val authViewModel: AuthViewModel =
        viewModel(factory = AuthVmFactory(AppGraph.authRepo))

    NavHost(
        navController = navController,
        startDestination = Route.Splash.path
    ) {
        //---Animación de Inicio----//
        composable(Route.Splash.path) {
            SplashScreen(
                authVm = authViewModel,
                onFinished = { isLoggedIn ->
                    val target = if (isLoggedIn) Route.Home.path else Route.Login.path
                    navController.navigate(target) {
                        popUpTo(Route.Splash.path) { inclusive = true } // evita volver a Splash
                        launchSingleTop = true
                    }
                }
            )
        }
        // --- Flujo de Autenticación ---
        composable(Route.Login.path) {
            LoginScreen(navController = navController, viewModel = authViewModel)
        }
        composable(Route.Registro.path) {
            RegistroScreen(navController = navController, viewModel = authViewModel)
        }

        // --- Flujo Principal (con MainShell) ---
        composable(Route.Home.path) {
            MainShell(navController , windowSizeClass) {
                HomeScreen(navController = navController) }
        }

        composable(Route.Servicios.path) {
            val carritoVm: CarritoViewModel =
                viewModel(factory = CarritoVmFactory(AppGraph.cartRepo))

            MainShell(navController, windowSizeClass) {
                ServiciosScreen(
                    navController = navController,
                    onAgregar = { sku: String, nombre: String, precio: Int ->
                        carritoVm.add(ItemCarrito(sku = sku, nombre = nombre, precio = precio))
                    }
                )
            }
        }
        composable(Route.Blogs.path) {
            MainShell(navController, windowSizeClass) { BlogScreen() }
        }
        composable(Route.Nosotros.path) {
            MainShell(navController, windowSizeClass) { NosotrosScreen() }
        }
        composable(Route.Contacto.path) {
            val contactoViewModel: ContactoViewModel = viewModel()
            MainShell(navController, windowSizeClass) { ContactoScreen(navController, contactoViewModel) }
        }

        composable(Route.Perfil.path) {
            val perfilVm: PerfilViewModel = viewModel(
                factory = PerfilVmFactory(AppGraph.app, AppGraph.authRepo)
            )

            MainShell(navController, windowSizeClass) {
                PerfilScreen(
                    authVm = authViewModel,
                    perfilVm = perfilVm,
                    onIrLogin = { navController.navigate(Route.Login.path) },
                    // Lógica de cierre de sesión corregida con callback
                    onCerrarSesion = {
                        authViewModel.cerrarSesion {
                            navController.navigate(Route.Login.path) {
                                popUpTo(navController.graph.id) { inclusive = true }
                            }
                        }
                    },
                    onEditar = {
                        // Por ahora no hace nada
                    }
                )
            }
        }

        composable(
            route = "servicio_detalle/{sku}",
            arguments = listOf(navArgument("sku") { type = NavType.StringType })
        ) { backStackEntry ->
            val sku = backStackEntry.arguments?.getString("sku") ?: ""
            val carritoVm: CarritoViewModel =
                viewModel(factory = CarritoVmFactory(AppGraph.cartRepo))
            MainShell(navController, windowSizeClass) {
                ServicioDetalleScreen(navController = navController,
                    servicioSku = sku,
                    onAgregar = { sku: String, nombre: String, precio: Int ->
                        carritoVm.add(ItemCarrito(sku = sku, nombre = nombre, precio = precio))
                    })
            }
        }
        composable(Route.Carrito.path) {
            val carritoVm: CarritoViewModel =  viewModel(factory = CarritoVmFactory(AppGraph.cartRepo))
            MainShell(navController, windowSizeClass)
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
            MainShell(navController, windowSizeClass) {
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
