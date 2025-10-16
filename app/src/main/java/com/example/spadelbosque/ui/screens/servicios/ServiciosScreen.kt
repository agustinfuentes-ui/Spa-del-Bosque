package com.example.spadelbosque.ui.screens.servicios

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.spadelbosque.ui.components.ServicioCard
import com.example.spadelbosque.viewmodel.ServiciosViewModel
import com.example.spadelbosque.ui.components.CategoriaChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiciosScreen(
    navController: NavController,
    serviciosViewModel: ServiciosViewModel = viewModel()
) {
    val uiState by serviciosViewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuestros Servicios",color = MaterialTheme.colorScheme.onPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(uiState.categorias) { categoria ->
                    CategoriaChip(
                        texto = categoria.replaceFirstChar { it.uppercase() },
                        selected = categoria == uiState.categoriaSeleccionada,
                        modifier = Modifier
                            .padding(horizontal = 6.dp)
                            .clickable { serviciosViewModel.seleccionarCategoria(categoria) }
                    )

                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(uiState.servicios) { servicio ->
                    ServicioCard(
                        servicio = servicio,
                        onVerDetalleClick = {
                            navController.navigate("servicio_detalle/${servicio.sku}")
                        },
                        onAgregarClick = {
                            Toast.makeText(context, "${it.nombre} se agregó al carrito", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}
