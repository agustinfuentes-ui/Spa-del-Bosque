package com.example.spadelbosque.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// 1. Define un estado para los datos de la pantalla
data class NosotrosUiState(
    val direccion: String = "Av. Manantiales 1420, Concón",
    val telefono: String = "+56 9 8765 4321",
    val email: String = "contacto@spadelbosque.cl",
    val horarios: List<String> = listOf(
        "Martes a Sábado: 10:00 - 20:00",
        "Domingo: 10:00 - 16:00",
        "Lunes: Cerrado"
    )
)

class NosotrosViewModel : ViewModel() {
    // 2. Expone el estado a la UI
    private val _uiState = MutableStateFlow(NosotrosUiState())
    val uiState = _uiState.asStateFlow()

    // Aquí podrías cargar estos datos desde una API en el futuro
}

