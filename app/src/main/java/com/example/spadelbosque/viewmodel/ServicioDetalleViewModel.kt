package com.example.spadelbosque.viewmodel

import androidx.lifecycle.ViewModel
import com.example.spadelbosque.repository.ServiciosRepository
import com.example.spadelbosque.model.Servicio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Estado de la UI para la pantalla de detalle
data class ServicioDetalleUiState(
    val servicio: Servicio? = null,
    val serviciosSugeridos: List<Servicio> = emptyList(),
    val error: String? = null
)

class ServicioDetalleViewModel : ViewModel() {

    private val repository = ServiciosRepository()

    // StateFlow para exponer el estado de la UI
    private val _uiState = MutableStateFlow(ServicioDetalleUiState())
    val uiState: StateFlow<ServicioDetalleUiState> = _uiState.asStateFlow()

    /**
     * Carga los detalles de un servicio específico y una lista de sugerencias.
     * @param sku El SKU del servicio a cargar.
     */
    fun cargarDetalleServicio(sku: String) {
        // Busca el servicio principal por su SKU
        val servicioPrincipal = repository.getServicioPorSku(sku)

        if (servicioPrincipal != null) {
            // Si se encuentra, busca otros servicios de la misma categoría
            val serviciosSugeridos = repository.getServiciosPorCategoria(servicioPrincipal.categoria)
                .filter { it.sku != sku } // Excluye el servicio que ya se está mostrando

            // Actualiza el estado de la UI con los datos encontrados
            _uiState.update {
                it.copy(
                    servicio = servicioPrincipal,
                    serviciosSugeridos = serviciosSugeridos,
                    error = null
                )
            }
        } else {
            // Si no se encuentra el servicio, actualiza el estado con un error
            _uiState.update {
                it.copy(
                    servicio = null,
                    serviciosSugeridos = emptyList(),
                    error = "Servicio no encontrado"
                )
            }
        }
    }
}
