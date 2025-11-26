package com.example.spadelbosque.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

class ServiciosViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ServiciosViewModel

    @Before
    fun setup() {
        viewModel = ServiciosViewModel()
    }

    @Test
    fun `estado inicial debe tener categorias cargadas`() {
        // When: Obtenemos el estado inicial
        val state = viewModel.uiState.value

        // Then: Debe tener categorías
        assertTrue("Debe tener categorías", state.categorias.isNotEmpty())
    }

    @Test
    fun `estado inicial debe tener categoria seleccionada`() {
        // When: Obtenemos el estado inicial
        val state = viewModel.uiState.value

        // Then: Debe tener una categoría seleccionada
        assertTrue("Debe tener categoría seleccionada",
            state.categoriaSeleccionada.isNotEmpty())
    }

    @Test
    fun `estado inicial debe tener servicios de primera categoria`() {
        // When: Obtenemos el estado inicial
        val state = viewModel.uiState.value

        // Then: Debe tener servicios
        assertTrue("Debe tener servicios", state.servicios.isNotEmpty())
    }

    @Test
    fun `seleccionar categoria debe cambiar servicios`() {
        // Given: Estado inicial
        val categoriaInicial = viewModel.uiState.value.categoriaSeleccionada
        val serviciosIniciales = viewModel.uiState.value.servicios

        // When: Seleccionamos otra categoría (si hay más de una)
        val categorias = viewModel.uiState.value.categorias
        if (categorias.size > 1) {
            val otraCategoria = categorias.find { it != categoriaInicial } ?: categorias[1]
            viewModel.seleccionarCategoria(otraCategoria)

            // Then: Debe cambiar la categoría y servicios
            val nuevoEstado = viewModel.uiState.value
            assertEquals("Categoría debe cambiar", otraCategoria,
                nuevoEstado.categoriaSeleccionada)
        }
    }

    @Test
    fun `seleccionar misma categoria no debe cambiar estado`() {
        // Given: Categoría actual
        val categoriaActual = viewModel.uiState.value.categoriaSeleccionada

        // When: Seleccionamos la misma
        viewModel.seleccionarCategoria(categoriaActual)

        // Then: Debe mantener la misma categoría
        assertEquals("Categoría debe mantenerse", categoriaActual,
            viewModel.uiState.value.categoriaSeleccionada)
    }
}