package com.example.spadelbosque.ui.screens.blogs.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.spadelbosque.model.BlogArticulo

@Composable
fun ArticuloCard (article : BlogArticulo){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Imagen del artículo
            Image(
                painter = painterResource(id = article.imageRes),
                contentDescription = "Imagen de ${article.title}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )

            // Contenido de texto
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Título del artículo
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Autor y fecha
                Text(
                    text = "Por ${article.author} — ${article.date}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Extracto del artículo
                Text(
                    text = article.excerpt,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Botón "Leer más"
                Button(
                    onClick = { /* TODO: Acción futura */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Leer más")
                }
            }
        }
    }
}
