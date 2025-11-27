package com.example.spadelbosque.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.spadelbosque.model.transbank.EstadoTransbank
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class PagoTransbankViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: PagoTransbankViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = PagoTransbankViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    // PRUEBA 1: Estado inicial

    @Test
    fun `estado inicial debe ser INICIANDO`() {
        // When: Obtenemos el estado
        val estado = viewModel.estadoPago.value

        // Then: Debe ser INICIANDO
        assertEquals("Estado inicial debe ser INICIANDO",
            EstadoTransbank.INICIANDO, estado)
    }


    // PRUEBA 2: TransaccionData inicial es null

    @Test
    fun `transaccion data inicial debe ser null`() {
        // When: Obtenemos los datos de transacción
        val data = viewModel.transaccionData.value

        // Then: Debe ser null
        assertNull("Datos de transacción deben ser null inicialmente", data)
    }


    // PRUEBA 3: Error inicial es null

    @Test
    fun `error inicial debe ser null`() {
        // When: Obtenemos el error
        val error = viewModel.error.value

        // Then: Debe ser null
        assertNull("Error debe ser null inicialmente", error)
    }


    // PRUEBA 4: Resetear limpia los estados

    @Test
    fun `resetear debe limpiar todos los estados`() {
        // When: Reseteamos
        viewModel.resetear()

        // Then: Todo debe estar en estado inicial
        assertEquals("Estado debe ser INICIANDO",
            EstadoTransbank.INICIANDO, viewModel.estadoPago.value)
        assertNull("Datos de transacción deben ser null",
            viewModel.transaccionData.value)
        assertNull("Error debe ser null", viewModel.error.value)
        assertNull("Resultado debe ser null", viewModel.resultado.value)
    }
}