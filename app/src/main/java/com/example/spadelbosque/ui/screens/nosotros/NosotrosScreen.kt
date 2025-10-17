package com.example.spadelbosque.ui.screens.nosotros

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.spadelbosque.ui.screens.home.HomeScreen
import com.example.spadelbosque.ui.theme.SpaTheme

@Composable
fun NosotrosScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Nosotros (placeholder)")
    }
}

@Preview(showBackground = true)
@Composable
fun NosotrosScreenPreview() {
    SpaTheme {
        NosotrosScreen()
    }
}