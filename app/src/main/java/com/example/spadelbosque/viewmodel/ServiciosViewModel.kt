package com.example.spadelbosque.viewmodel

import androidx.lifecycle.ViewModel
import com.example.spadelbosque.repository.ServiciosRepository
import com.example.spadelbosque.model.Servicio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// 1. Estado de la UI
data class ServiciosUiState(
    val categorias: List<String> = emptyList(),
    val categoriaSeleccionada: String = "",
    val servicios: List<Servicio> = emptyList()
)

// 2. Crear el ViewModel
class ServiciosViewModel : ViewModel() {

    // Instanciar el Repositorio
    private val repository = ServiciosRepository()

    // Exponer el estado de la UI usando StateFlow
    private val _uiState = MutableStateFlow(ServiciosUiState())
    val uiState: StateFlow<ServiciosUiState> = _uiState.asStateFlow()

    // Bloque de inicialización para cargar los datos cuando se crea el ViewModel
    init {
        cargarDatosIniciales()
    }

    private fun cargarDatosIniciales() {
        val categorias = repository.getCategorias()
        val categoriaInicial = categorias.firstOrNull() ?: ""
        val serviciosIniciales = if (categoriaInicial.isNotEmpty()) {
            repository.getServiciosPorCategoria(categoriaInicial)
        } else {
            emptyList()
        }

        _uiState.value = ServiciosUiState(
            categorias = categorias,
            categoriaSeleccionada = categoriaInicial,
            servicios = serviciosIniciales
        )
    }

    // 3. Manejar las acciones del usuario
    fun seleccionarCategoria(categoria: String) {
        val serviciosParaCategoria = repository.getServiciosPorCategoria(categoria)
        _uiState.update {
            it.copy(
                categoriaSeleccionada = categoria,
                servicios = serviciosParaCategoria
            )
        }
    }
}
