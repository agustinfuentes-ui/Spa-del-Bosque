package com.example.spadelbosque.ui.screens.carrito

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.spadelbosque.model.ItemCarrito
import com.example.spadelbosque.viewmodel.CarritoViewModel
import com.example.spadelbosque.ui.util.CLP

@Composable
fun CarritoScreen(
    vm: CarritoViewModel,
    onSeguirAgregando: () -> Unit,
    onComprar:() -> Unit

) {
    val state by vm.ui.collectAsState()

    if (state.items.isEmpty()) {
        // Estado vacío
        Column(
            Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Tu carrito está vacío", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Text("Visita Servicios para agregar terapias.")
                }
            }
            Button(onClick = onSeguirAgregando) { Text("Ir a Servicios") }
        }
        return
    }

    // Contenedor Box para permitir la superposición y alineación
    Box(modifier = Modifier.fillMaxSize()) {
        // Lista de ítems
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(bottom = 76.dp),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.items, key = { it.sku }) { it ->
                ItemRow(
                    item = it,
                    onInc = { vm.inc(it.sku) },
                    onDec = { vm.dec(it.sku) },
                    onRemove = { vm.remove(it.sku) }
                )
            }
            item { Spacer(Modifier.height(80.dp)) } // espacio para la barra de total
        }

        // Barra inferior con total y acciones
        Surface(
            tonalElevation = 3.dp,
            shadowElevation = 6.dp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total: ${state.totalCLP}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
                )

                OutlinedButton(onClick = { vm.clear() }) { Text("Vaciar") }
                Button(
                    onClick = onComprar,
                    enabled = state.items.isNotEmpty()
                ) {
                    Text( "Comprar")
                }
            }
        }
    }
}

@Composable
private fun ItemRow(
    item: ItemCarrito,
    onInc: () -> Unit,
    onDec: () -> Unit,
    onRemove: () -> Unit
) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Text(item.nombre, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(6.dp))
            Text("SKU: ${item.sku}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Precio: ${CLP.format(item.precio)}")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onDec) { Icon(Icons.Default.Remove, contentDescription = "Disminuir") }
                    Text("${item.qty}", Modifier.padding(horizontal = 8.dp))
                    IconButton(onClick = onInc) { Icon(Icons.Default.Add, contentDescription = "Aumentar") }
                    IconButton(onClick = onRemove) { Icon(Icons.Default.Delete, contentDescription = "Eliminar") }
                }
            }
            Spacer(Modifier.height(4.dp))
            Text("Subtotal: ${CLP.format(item.precio * item.qty)}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

