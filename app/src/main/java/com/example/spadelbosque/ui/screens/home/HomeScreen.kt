package com.example.spadelbosque.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import com.example.spadelbosque.ui.components.Carrusel
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.example.spadelbosque.navigation.Route
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    "Relajo y desconexión",
                    color = MaterialTheme.colorScheme.onBackground
                )
            })
        }
    ) { innerPadding ->
        LazyColumn (
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Descubra los espacios ideales para el descanso y la calma en Spa del Bosque. Contamos con el único circuito de aguas de la V Región, acondicionadas naturalmente a tres temperaturas. Un mágico lugar donde el silencio, la paz, las vertientes y la naturaleza son protagonistas.\n" +
                            "Explore nuestros servicios, masajes, tratamientos corporales y terapias de relajación que estimularán la concentración, salud y bienestar integralmente.",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            item {
                Carrusel()
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Button(onClick = {
                        navController.navigate(Route.Servicios.path)
                    }) {
                        Text("Servicios")
                    }
                    Button(onClick = {
                        navController.navigate(Route.Nosotros.path)
                    }) {
                        Text("Nosotros")
                    }
                }
            }
        }
    }
}


