package com.example.spadelbosque.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

class NosotrosViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: NosotrosViewModel

    @Before
    fun setup() {
        viewModel = NosotrosViewModel()
    }

    @Test
    fun `estado inicial debe tener direccion`() {
        // When: Obtenemos el estado
        val state = viewModel.uiState.value

        // Then: Debe tener dirección
        assertTrue("Debe tener dirección", state.direccion.isNotEmpty())
    }

    @Test
    fun `estado inicial debe tener telefono`() {
        // When: Obtenemos el estado
        val state = viewModel.uiState.value

        // Then: Debe tener teléfono
        assertTrue("Debe tener teléfono", state.telefono.isNotEmpty())
    }

    @Test
    fun `estado inicial debe tener email`() {
        // When: Obtenemos el estado
        val state = viewModel.uiState.value

        // Then: Debe tener email
        assertTrue("Debe tener email", state.email.isNotEmpty())
        assertTrue("Email debe tener @", state.email.contains("@"))
    }

    @Test
    fun `estado inicial debe tener horarios`() {
        // When: Obtenemos el estado
        val state = viewModel.uiState.value

        // Then: Debe tener horarios
        assertTrue("Debe tener horarios", state.horarios.isNotEmpty())
    }

    @Test
    fun `horarios debe incluir dias de la semana`() {
        // When: Obtenemos horarios
        val horarios = viewModel.uiState.value.horarios

        // Then: Debe mencionar días
        val textoCompleto = horarios.joinToString(" ")
        assertTrue("Debe mencionar días",
            textoCompleto.contains("Lunes") ||
                    textoCompleto.contains("Martes"))
    }
}