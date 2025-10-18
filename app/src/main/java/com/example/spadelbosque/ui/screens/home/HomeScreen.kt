package com.example.spadelbosque.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import com.example.spadelbosque.ui.components.Carrusel
import com.example.spadelbosque.ui.theme.SpaTheme
import androidx.navigation.NavController
import androidx.compose.runtime.LaunchedEffect
import com.example.spadelbosque.viewmodel.CarritoViewModel
import com.example.spadelbosque.viewmodel.factory.CarritoVmFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.spadelbosque.di.AppGraph
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.example.spadelbosque.navigation.Route


@SuppressLint("UnrememberedGetBackStackEntry")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    carritoVm: CarritoViewModel = viewModel(factory = CarritoVmFactory(AppGraph.cartRepo))
) {
    val clearFlow = remember(navController) {
        navController.getBackStackEntry(Route.Home.path)
            .savedStateHandle
            .getStateFlow("clear_cart", false)
    }
    val shouldClear by clearFlow.collectAsState()

    LaunchedEffect(shouldClear) {
        if (shouldClear) {
            carritoVm.clear()
            // apaga el flag para no repetir
            navController.getBackStackEntry(Route.Home.path)
                .savedStateHandle["clear_cart"] = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {Text("Relajo y desconexión", color = MaterialTheme.colorScheme.onBackground)})
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(text = "Descubra los espacios ideales para el descanso y la calma en Spa del Bosque. " +
                    "Contamos con el único circuito de aguas de la V Región, acondicionadas naturalmente a " +
                    "tres temperaturas. Un mágico lugar donde el silencio, la paz, las vertientes y la naturaleza " +
                    "son protagonistas.\n",
                color = MaterialTheme.colorScheme.onBackground)

            Carrusel()

            Button(onClick = {/* accion futura*/ }) {
                Text("Servicios")
            }
            Button(onClick = {/* accion futura*/ }) {
                Text("Nosotros",
                    color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SpaTheme {
        // Se añade un NavController de prueba para la previsualización
        HomeScreen(navController = rememberNavController())
    }
}
