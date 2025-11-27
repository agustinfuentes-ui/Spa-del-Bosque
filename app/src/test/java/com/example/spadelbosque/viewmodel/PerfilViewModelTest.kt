package com.example.spadelbosque.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.spadelbosque.model.Usuario
import com.example.spadelbosque.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class PerfilViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var mockApp: Application
    private lateinit var mockAuth: AuthRepository
    private lateinit var viewModel: PerfilViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        mockApp = mockk(relaxed = true)
        mockAuth = mockk(relaxed = true)

        // Mock SharedPreferences
        every { mockApp.getSharedPreferences(any(), any()) } returns mockk(relaxed = true)

        viewModel = PerfilViewModel(mockApp, mockAuth)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `estado inicial debe estar vacio`() {
        // When: Obtenemos estado inicial
        val state = viewModel.ui.value

        // Then: Debe estar vacío
        assertTrue("Nombres debe estar vacío", state.nombres.isEmpty())
        assertTrue("Apellidos debe estar vacío", state.apellidos.isEmpty())
    }

    @Test
    fun `cambiar nombres debe actualizar estado`() {
        // When: Cambiamos nombres
        viewModel.onNombres("Juan")

        // Then: Debe actualizarse
        assertEquals("Juan", viewModel.ui.value.nombres)
    }

    @Test
    fun `cambiar apellidos debe actualizar estado`() {
        // When: Cambiamos apellidos
        viewModel.onApellidos("Pérez")

        // Then: Debe actualizarse
        assertEquals("Pérez", viewModel.ui.value.apellidos)
    }

    @Test
    fun `cambiar telefono debe actualizar estado`() {
        // When: Cambiamos teléfono
        viewModel.onTelefono("987654321")

        // Then: Debe actualizarse
        assertEquals("987654321", viewModel.ui.value.telefono)
    }

    @Test
    fun `nombre completo debe concatenar nombres y apellidos`() {
        // Given: Nombres y apellidos
        viewModel.onNombres("Juan")
        viewModel.onApellidos("Pérez")

        // When: Obtenemos nombre completo
        val nombreCompleto = viewModel.ui.value.nombreCompleto

        // Then: Debe estar concatenado
        assertEquals("Juan Pérez", nombreCompleto)
    }

    @Test
    fun `cargar con usuario debe actualizar estado`() = runTest {
        // Given: Usuario mockeado
        val usuario = Usuario(
            id = "1",
            nombres = "Juan",
            apellidos = "Pérez",
            correo = "juan@duoc.cl",
            password = "123456",
            telefono = "987654321"
        )
        coEvery { mockAuth.obtenerSesionActiva() } returns usuario

        // When: Cargamos
        viewModel.cargar()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: Debe actualizar con datos del usuario
        val state = viewModel.ui.value
        assertEquals("Juan", state.nombres)
        assertEquals("Pérez", state.apellidos)
        assertEquals("juan@duoc.cl", state.correo)
    }

    @Test
    fun `estado inicial debe tener compras vacias`() {
        // When: Obtenemos estado
        val compras = viewModel.ui.value.compras

        // Then: Debe estar vacío inicialmente
        assertTrue("Compras debe estar vacío", compras.isEmpty())
    }
}