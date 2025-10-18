package com.example.spadelbosque.ui.screens.carrito

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.spadelbosque.ui.util.CLP
import com.example.spadelbosque.viewmodel.CarritoViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompraScreen(
    carritoVm: CarritoViewModel,
    onCancelar: () -> Unit,
    onFinalizar: () -> Unit
) {
    val state by carritoVm.ui.collectAsState()

    // Fecha/Hora
    val fechaHora = remember {
        val f = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "CL"))
        f.format(Date())
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Confirmación de compra") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(56.dp)
            )

            Text("Revisa tu resumen", style = MaterialTheme.typography.titleLarge)

            // Resumen: fecha, cantidad total, total $
            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(
                    Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Fecha/Hora: $fechaHora")
                    Text("Ítems: ${state.count}")
                    Text("Total: ${state.totalCLP}", style = MaterialTheme.typography.titleMedium)
                }
            }

            // LISTADO DE ÍTEMS
            if (state.items.isNotEmpty()) {
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(8.dp)) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 280.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(8.dp)
                        ) {
                            items(state.items, key = { it.sku }) { it ->
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(Modifier.weight(1f)) {
                                        Text(it.nombre, style = MaterialTheme.typography.titleSmall)
                                        Text(
                                            "${it.qty} × ${CLP.format(it.precio)}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Text(
                                        CLP.format(it.qty * it.precio),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                Divider()
                            }
                        }
                    }
                }
            } else {
                Text("No hay ítems en el carrito.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            // BOTONES
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onCancelar,
                    modifier = Modifier.weight(1f)
                ) { Text("Cancelar") }

                Button(
                    onClick = onFinalizar,
                    enabled = state.items.isNotEmpty(),
                    modifier = Modifier.weight(1f)
                ) { Text("Finalizar compra") }
            }
        }
    }
}
