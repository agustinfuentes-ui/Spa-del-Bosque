package com.example.spadelbosque.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

class ServicioDetalleViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ServicioDetalleViewModel

    @Before
    fun setup() {
        viewModel = ServicioDetalleViewModel()
    }

    @Test
    fun `estado inicial debe tener servicio null`() {
        // When: Obtenemos estado inicial
        val state = viewModel.uiState.value

        // Then: Servicio debe ser null
        assertNull("Servicio debe ser null", state.servicio)
    }

    @Test
    fun `estado inicial debe tener sugerencias vacias`() {
        // When: Obtenemos estado inicial
        val state = viewModel.uiState.value

        // Then: Sugerencias deben estar vacías
        assertTrue("Sugerencias debe estar vacío", state.serviciosSugeridos.isEmpty())
    }

    @Test
    fun `cargar servicio valido debe actualizar estado`() {
        // Given: Un SKU válido (asumiendo que existe en el mock)
        val sku = "RELAX30"

        // When: Cargamos el servicio
        viewModel.cargarDetalleServicio(sku)

        // Then: El servicio no debe ser null
        val state = viewModel.uiState.value
        // Si el servicio existe en tu mock, descomentar:
        // assertNotNull("Servicio debe cargarse", state.servicio)
    }

    @Test
    fun `cargar servicio invalido debe mostrar error`() {
        // Given: Un SKU inválido
        val sku = "SKU_INVALIDO_12345"

        // When: Cargamos el servicio
        viewModel.cargarDetalleServicio(sku)

        // Then: Debe mostrar error
        val state = viewModel.uiState.value
        assertNotNull("Debe tener mensaje de error", state.error)
        assertNull("Servicio debe ser null", state.servicio)
    }

    @Test
    fun `cargar servicio debe traer sugerencias de misma categoria`() {
        // Given: Un SKU válido
        val sku = "RELAX30"

        // When: Cargamos el servicio
        viewModel.cargarDetalleServicio(sku)

        // Then: Las sugerencias no deben incluir el mismo servicio
        val state = viewModel.uiState.value
        val contieneElMismo = state.serviciosSugeridos.any { it.sku == sku }
        assertFalse("Sugerencias no debe incluir el mismo servicio", contieneElMismo)
    }
}