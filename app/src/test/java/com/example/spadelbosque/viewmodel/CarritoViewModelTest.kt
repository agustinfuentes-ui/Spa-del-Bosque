package com.example.spadelbosque.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.spadelbosque.model.ItemCarrito
import com.example.spadelbosque.repository.CartRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class CarritoViewModelTest {

    // Regla para que LiveData funcione en tests
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Dispatcher de prueba para coroutines
    private val testDispatcher = StandardTestDispatcher()

    // Mock del repository
    private lateinit var mockRepository: CartRepository

    // ViewModel a probar
    private lateinit var viewModel: CarritoViewModel

    @Before
    fun setup() {
        // Configurar dispatcher de prueba
        Dispatchers.setMain(testDispatcher)

        // Crear mock del repository
        mockRepository = mockk(relaxed = true)

        // Configurar comportamiento del mock
        every { mockRepository.items } returns MutableStateFlow(emptyList())

        // Crear ViewModel con el mock
        viewModel = CarritoViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // PRUEBA 1: Estado inicial del carrito
    @Test
    fun `estado inicial debe tener carrito vacio`() {
        // Given: ViewModel recién creado

        // When: Obtenemos el estado actual
        val state = viewModel.ui.value

        // Then: El carrito debe estar vacío
        assertTrue("El carrito debe estar vacío", state.items.isEmpty())
        assertEquals("El total debe ser 0", 0, state.total)
        assertEquals("El contador debe ser 0", 0, state.count)
    }

    // PRUEBA 2: Agregar item al carrito

    @Test
    fun `agregar item debe llamar al repository`() {
        // Given: Un item de prueba
        val item = ItemCarrito(
            sku = "RELAX30",
            nombre = "Masaje Relajante",
            precio = 25000,
            qty = 1
        )

        // When: Agregamos el item
        viewModel.add(item)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: Se debe llamar al método add del repository
        verify { mockRepository.add(item) }
    }

    // PRUEBA 3: Incrementar cantidad
    @Test
    fun `incrementar debe llamar al repository con SKU correcto`() {
        // Given: Un SKU
        val sku = "RELAX30"

        // When: Incrementamos
        viewModel.inc(sku)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: Se debe llamar a inc con el SKU
        verify { mockRepository.inc(sku) }
    }

    // PRUEBA 4: Decrementar cantidad
    @Test
    fun `decrementar debe llamar al repository con SKU correcto`() {
        // Given: Un SKU
        val sku = "RELAX30"

        // When: Decrementamos
        viewModel.dec(sku)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: Se debe llamar a dec con el SKU
        verify { mockRepository.dec(sku) }
    }

    // PRUEBA 5: Remover item
    @Test
    fun `remover debe llamar al repository con SKU correcto`() {
        // Given: Un SKU
        val sku = "RELAX30"

        // When: Removemos
        viewModel.remove(sku)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: Se debe llamar a remove con el SKU
        verify { mockRepository.remove(sku) }
    }

    // PRUEBA 6: Limpiar carrito
    @Test
    fun `limpiar debe llamar al metodo clear del repository`() {
        // When: Limpiamos el carrito
        viewModel.clear()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: Se debe llamar a clear
        verify { mockRepository.clear() }
    }

    // PRUEBA 7: Cálculo de total con items
    @Test
    fun `estado debe calcular total correctamente con items`() {
        // Given: Repository con items
        val items = listOf(
            ItemCarrito("RELAX30", "Masaje", 25000, 2),
            ItemCarrito("CIRC45", "Circuito", 15000, 1)
        )
        every { mockRepository.items } returns MutableStateFlow(items)

        // When: Creamos nuevo ViewModel
        val vm = CarritoViewModel(mockRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: El total debe ser correcto
        val state = vm.ui.value
        assertEquals("Debe haber 2 items", 2, state.items.size)
        assertEquals("Total debe ser 65000", 65000, state.total)
        assertEquals("Contador debe ser 3 items", 3, state.count)
    }
}