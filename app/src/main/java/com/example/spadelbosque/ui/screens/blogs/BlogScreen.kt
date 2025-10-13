package com.example.spadelbosque.ui.screens.blogs
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spadelbosque.ui.screens.blogs.components.ArticuloCard
import com.example.spadelbosque.viewmodel.BlogViewModel
import com.example.spadelbosque.ui.theme.SpaTheme


// Pantalla principal del Blog de Bienestar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogScreen(viewModel: BlogViewModel = BlogViewModel()) {

    // Obtener la lista de artículos del ViewModel
    val articles = viewModel.getArticles()

    // Scaffold: Estructura básica de la pantalla
    Scaffold(
        topBar = {
            // Barra superior con el título
            TopAppBar(
                title = {
                    Column {
                        Text("Blog de bienestar")
                        Text(
                            "Consejos, rutinas y novedades para cuidarte cuerpo y mente.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->

        // LazyColumn: Lista desplazable eficiente (como RecyclerView)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            // items: Crea un elemento por cada artículo en la lista
            items(articles) { article ->
                ArticuloCard(article = article)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BlogScreenPreview() {
    SpaTheme  {
        BlogScreen()
    }
}