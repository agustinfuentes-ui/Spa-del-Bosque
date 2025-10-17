package com.example.spadelbosque.ui.screens.servicios

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.spadelbosque.R
import com.example.spadelbosque.ui.components.ServicioCard
import com.example.spadelbosque.ui.components.CategoriaChip
import com.example.spadelbosque.ui.util.CLP
import com.example.spadelbosque.viewmodel.ServicioDetalleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicioDetalleScreen(
    navController: NavController,
    servicioSku: String,
    detalleViewModel: ServicioDetalleViewModel = viewModel()
) {
    LaunchedEffect(key1 = servicioSku) {
        detalleViewModel.cargarDetalleServicio(servicioSku)
    }

    val uiState by detalleViewModel.uiState.collectAsState()
    val servicio = uiState.servicio
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Servicio",color = MaterialTheme.colorScheme.onPrimary) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            } else if (servicio == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                val imageResId = context.resources.getIdentifier(servicio.img, "drawable", context.packageName)

                Image(
                    painter = painterResource(id = if (imageResId != 0) imageResId else R.drawable.logo),
                    contentDescription = "Imagen de ${servicio.nombre}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CategoriaChip(servicio.categoria.replaceFirstChar { it.uppercase() })

                        Text(
                            text = CLP.format(servicio.precio),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.W500,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Disfruta de una experiencia de relajación profunda con nuestro masaje exclusivo. Diseñado para aliviar el estrés y la tensión acumulada, este tratamiento te dejará renovado y revitalizado.",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón de acción principal
                    Button(
                        onClick = {
                            Toast.makeText(context, "${servicio.nombre} se agregó al carrito", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.AddShoppingCart, contentDescription = null)
                        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                        Text("Agregar",
                            color = MaterialTheme.colorScheme.onPrimary)
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "También te puede interesar",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.W600
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 10.dp)
                    ) {
                        items(uiState.serviciosSugeridos) { sugerencia ->
                            ServicioCard(
                                servicio = sugerencia,
                                onVerDetalleClick = { sku ->
                                    navController.navigate("servicio_detalle/${sku}")
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
    }
}
