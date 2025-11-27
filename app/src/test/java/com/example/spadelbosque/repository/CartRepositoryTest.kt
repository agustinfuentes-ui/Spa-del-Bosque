package com.example.spadelbosque.repository

import com.example.spadelbosque.model.ItemCarrito
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class CartRepositoryTest {

    private lateinit var repository: CartRepositoryImpl

    @Before
    fun setup() {
        repository = CartRepositoryImpl()
    }


    // PRUEBA 1: Estado inicial vacío
    @Test
    fun `estado inicial debe tener lista vacia`() = runTest {
        // When: Obtenemos los items
        val items = repository.items.first()

        // Then: La lista debe estar vacía
        assertTrue("La lista debe estar vacía", items.isEmpty())
    }


    // PRUEBA 2: Agregar item nuevo

    @Test
    fun `agregar item nuevo debe aumentar la lista`() = runTest {
        // Given: Un item
        val item = ItemCarrito("RELAX30", "Masaje", 25000, 1)

        // When: Lo agregamos
        repository.add(item)
        val items = repository.items.first()

        // Then: Debe estar en la lista
        assertEquals("Debe haber 1 item", 1, items.size)
        assertEquals("Debe ser el item agregado", item, items[0])
    }


    // PRUEBA 3: Agregar item existente incrementa cantidad

    @Test
    fun `agregar item existente debe incrementar cantidad`() = runTest {
        // Given: Un item ya agregado
        val item = ItemCarrito("RELAX30", "Masaje", 25000, 1)
        repository.add(item)

        // When: Lo agregamos de nuevo
        repository.add(item)
        val items = repository.items.first()

        // Then: Debe incrementar la cantidad, no duplicarse
        assertEquals("Debe seguir siendo 1 item", 1, items.size)
        assertEquals("La cantidad debe ser 2", 2, items[0].qty)
    }


    // PRUEBA 4: Incrementar cantidad

    @Test
    fun `incrementar debe aumentar cantidad en 1`() = runTest {
        // Given: Un item con cantidad 1
        val item = ItemCarrito("RELAX30", "Masaje", 25000, 1)
        repository.add(item)

        // When: Incrementamos
        repository.inc("RELAX30")
        val items = repository.items.first()

        // Then: La cantidad debe ser 2
        assertEquals("La cantidad debe ser 2", 2, items[0].qty)
    }


    // PRUEBA 5: Decrementar cantidad

    @Test
    fun `decrementar debe disminuir cantidad en 1`() = runTest {
        // Given: Un item con cantidad 3
        val item = ItemCarrito("RELAX30", "Masaje", 25000, 3)
        repository.add(item)

        // When: Decrementamos
        repository.dec("RELAX30")
        val items = repository.items.first()

        // Then: La cantidad debe ser 2
        assertEquals("La cantidad debe ser 2", 2, items[0].qty)
    }


    // PRUEBA 6: Decrementar no baja de 1

    @Test
    fun `decrementar no debe bajar de 1`() = runTest {
        // Given: Un item con cantidad 1
        val item = ItemCarrito("RELAX30", "Masaje", 25000, 1)
        repository.add(item)

        // When: Decrementamos
        repository.dec("RELAX30")
        val items = repository.items.first()

        // Then: La cantidad debe seguir siendo 1
        assertEquals("La cantidad debe seguir en 1", 1, items[0].qty)
    }


    // PRUEBA 7: Remover item

    @Test
    fun `remover debe eliminar item de la lista`() = runTest {
        // Given: Dos items agregados
        repository.add(ItemCarrito("RELAX30", "Masaje", 25000, 1))
        repository.add(ItemCarrito("CIRC45", "Circuito", 15000, 1))

        // When: Removemos uno
        repository.remove("RELAX30")
        val items = repository.items.first()

        // Then: Solo debe quedar 1
        assertEquals("Debe quedar 1 item", 1, items.size)
        assertEquals("Debe quedar CIRC45", "CIRC45", items[0].sku)
    }


    // PRUEBA 8: Clear limpia todo

    @Test
    fun `clear debe vaciar la lista`() = runTest {
        // Given: Varios items
        repository.add(ItemCarrito("RELAX30", "Masaje", 25000, 1))
        repository.add(ItemCarrito("CIRC45", "Circuito", 15000, 2))

        // When: Limpiamos
        repository.clear()
        val items = repository.items.first()

        // Then: La lista debe estar vacía
        assertTrue("La lista debe estar vacía", items.isEmpty())
    }


    // PRUEBA 9: Agregar múltiples items diferentes

    @Test
    fun `agregar multiples items diferentes debe mantenerlos todos`() = runTest {
        // Given: Tres items diferentes
        val item1 = ItemCarrito("RELAX30", "Masaje", 25000, 1)
        val item2 = ItemCarrito("CIRC45", "Circuito", 15000, 1)
        val item3 = ItemCarrito("PIEDRAS60", "Piedras", 35000, 1)

        // When: Los agregamos
        repository.add(item1)
        repository.add(item2)
        repository.add(item3)
        val items = repository.items.first()

        // Then: Deben estar los 3
        assertEquals("Debe haber 3 items", 3, items.size)
    }
}