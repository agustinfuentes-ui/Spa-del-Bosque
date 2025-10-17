package com.example.spadelbosque.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.spadelbosque.R
import com.example.spadelbosque.model.Servicio
import com.example.spadelbosque.ui.util.CLP

@Composable
fun ServicioCard(
    servicio: Servicio,
    onVerDetalleClick: (String) -> Unit,
    onAgregarClick: (Servicio) -> Unit
) {
    val context = LocalContext.current
    val imageResId = context.resources.getIdentifier(servicio.img, "drawable", context.packageName)

    Card(
        modifier = Modifier
            .width(280.dp)
            .wrapContentHeight()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {


            Image(
                // 1. Lógica de la imagen corregida
                painter = painterResource(id = if (imageResId != 0) imageResId else R.drawable.logo),
                contentDescription = "Imagen de ${servicio.nombre}",
                modifier = Modifier
                    .height(140.dp)
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = servicio.nombre,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.WatchLater,
                    contentDescription = "Duración",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = "${servicio.duracionMin} min",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = CLP.format(servicio.precio),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.W500,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(25.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                OutlinedButton(
                    onClick = { onVerDetalleClick(servicio.sku) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Text("Ver detalle",
                        color = MaterialTheme.colorScheme.primary)
                }
                Button(
                    onClick = { onAgregarClick(servicio) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Agregar",
                        color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}
