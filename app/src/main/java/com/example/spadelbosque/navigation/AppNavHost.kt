package com.example.spadelbosque.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import com.example.spadelbosque.ui.screens.login.LoginScreen
import com.example.spadelbosque.ui.screens.registro.RegistroScreen
import com.example.spadelbosque.ui.components.MainShell
import com.example.spadelbosque.ui.screens.home.HomeScreen
import com.example.spadelbosque.ui.screens.servicios.ServiciosScreen
import com.example.spadelbosque.ui.screens.servicios.ServicioDetalleScreen
import com.example.spadelbosque.ui.screens.blogs.BlogScreen
import com.example.spadelbosque.ui.screens.nosotros.NosotrosScreen
import com.example.spadelbosque.ui.screens.contacto.ContactoScreen
import com.example.spadelbosque.ui.screens.carrito.*
import com.example.spadelbosque.ui.screens.perfil.PerfilScreen
import com.example.spadelbosque.viewmodel.AuthViewModel
import com.example.spadelbosque.viewmodel.CarritoViewModel
import com.example.spadelbosque.viewmodel.ContactoViewModel
import com.example.spadelbosque.viewmodel.PerfilViewModel
import com.example.spadelbosque.di.AppGraph
import com.example.spadelbosque.model.ItemCarrito
import com.example.spadelbosque.viewmodel.factory.AuthVmFactory
import com.example.spadelbosque.viewmodel.factory.CarritoVmFactory
import com.example.spadelbosque.viewmodel.factory.PerfilVmFactory
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.compose.ui.Alignment



@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedScreen(content: @Composable () -> Unit) {
    AnimatedContent(
        targetState = content,
        transitionSpec = {
            fadeIn(animationSpec = tween(300)).togetherWith(fadeOut(animationSpec = tween(300)))
        },
        contentAlignment = Alignment.TopCenter
    ) { targetContent ->
        targetContent()
    }
}
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavHost(windowSizeClass: WindowSizeClass) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel(factory = AuthVmFactory(AppGraph.authRepo))

    NavHost(
        navController = navController,
        startDestination = Route.Login.path
    ) {
        // --- Login ---
        composable(Route.Login.path) {
            AnimatedScreen {
                LoginScreen(navController, authViewModel)
            }
        }

        // --- Registro ---
        composable(Route.Registro.path) {
            AnimatedScreen {
                RegistroScreen(navController, authViewModel)
            }
        }

        // --- Home ---
        composable(Route.Home.path) {
            AnimatedScreen {
                MainShell(navController, windowSizeClass) {
                    HomeScreen(navController)
                }
            }
        }

        // --- Servicios ---
        composable(Route.Servicios.path) {
            val carritoVm: CarritoViewModel = viewModel(factory = CarritoVmFactory(AppGraph.cartRepo))
            AnimatedScreen {
                MainShell(navController, windowSizeClass) {
                    ServiciosScreen(navController) { sku, nombre, precio ->
                        carritoVm.add(ItemCarrito(sku, nombre, precio))
                    }
                }
            }
        }

        // --- Blogs ---
        composable(Route.Blogs.path) {
            MainShell(navController, windowSizeClass) {
                BlogScreen()
            }
        }

        // --- Nosotros ---
        composable(Route.Nosotros.path) {
            MainShell(navController, windowSizeClass) {
                NosotrosScreen()
            }
        }

        // --- Contacto ---
        composable(Route.Contacto.path) {
            val contactoViewModel: ContactoViewModel = viewModel()
            MainShell(navController, windowSizeClass) {
                ContactoScreen(navController, contactoViewModel)
            }
        }

        // --- Perfil ---
        composable(Route.Perfil.path) {
            val perfilVm: PerfilViewModel = viewModel(
                factory = PerfilVmFactory(AppGraph.app, AppGraph.authRepo)
            )
            MainShell(navController, windowSizeClass) {
                PerfilScreen(
                    authVm = authViewModel,
                    perfilVm = perfilVm,
                    onIrLogin = { navController.navigate(Route.Login.path) },
                    onCerrarSesion = {
                        authViewModel.cerrarSesion {
                            navController.navigate(Route.Login.path) {
                                popUpTo(navController.graph.id) { inclusive = true }
                            }
                        }
                    },
                    onEditar = { /* Por ahora no hace nada */ }
                )
            }
        }

        // --- Servicio Detalle ---
        composable(
            route = "servicio_detalle/{sku}",
            arguments = listOf(navArgument("sku") { type = NavType.StringType })
        ) { backStackEntry ->
            val sku = backStackEntry.arguments?.getString("sku") ?: ""
            val carritoVm: CarritoViewModel = viewModel(factory = CarritoVmFactory(AppGraph.cartRepo))
            MainShell(navController, windowSizeClass) {
                ServicioDetalleScreen(
                    navController = navController,
                    servicioSku = sku,
                    onAgregar = { sku, nombre, precio ->
                        carritoVm.add(ItemCarrito(sku = sku, nombre = nombre, precio = precio))
                    }
                )
            }
        }

    // --- Carrito ---
            composable(Route.Carrito.path) {
                val carritoVm: CarritoViewModel = viewModel(factory = CarritoVmFactory(AppGraph.cartRepo))
                MainShell(navController, windowSizeClass) {
                    CarritoScreen(
                        vm = carritoVm,
                        onSeguirAgregando = { navController.navigate(Route.Servicios.path) },
                        onComprar = { navController.navigate(Route.Compra.path) { launchSingleTop = true } }
                    )
                }
            }

    // --- Compra ---
            composable(Route.Compra.path) {
                val carritoVm: CarritoViewModel = viewModel(factory = CarritoVmFactory(AppGraph.cartRepo))
                MainShell(navController, windowSizeClass) {
                    CompraScreen(
                        carritoVm = carritoVm,
                        onCancelar = { navController.popBackStack() },
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
