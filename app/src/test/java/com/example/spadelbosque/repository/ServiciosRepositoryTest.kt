package com.example.spadelbosque.repository

import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class ServiciosRepositoryTest {

    private lateinit var repository: ServiciosRepository

    @Before
    fun setup() {
        repository = ServiciosRepository()
    }


    // PRUEBA 1: Get servicios retorna lista

    @Test
    fun `getServicios debe retornar lista`() {
        // When: Obtenemos servicios
        val servicios = repository.getServicios()

        // Then: Debe retornar lista
        assertNotNull("Lista no debe ser null", servicios)
    }


    // PRUEBA 2: Lista no vacía

    @Test
    fun `getServicios debe retornar lista no vacia`() {
        // When: Obtenemos servicios
        val servicios = repository.getServicios()

        // Then: Debe tener servicios
        assertTrue("Debe tener servicios", servicios.isNotEmpty())
    }


    // PRUEBA 3: Get categorías retorna lista

    @Test
    fun `getCategorias debe retornar lista de categorias`() {
        // When: Obtenemos categorías
        val categorias = repository.getCategorias()

        // Then: Debe retornar lista
        assertNotNull("Categorías no debe ser null", categorias)
        assertTrue("Debe tener categorías", categorias.isNotEmpty())
    }


    // PRUEBA 4: Categorías son únicas

    @Test
    fun `getCategorias debe retornar categorias unicas`() {
        // When: Obtenemos categorías
        val categorias = repository.getCategorias()

        // Then: No debe haber duplicados
        val categoriasUnicas = categorias.toSet()
        assertEquals("Categorías deben ser únicas",
            categorias.size, categoriasUnicas.size)
    }


    // PRUEBA 5: Filtrar por categoría

    @Test
    fun `getServiciosPorCategoria debe filtrar correctamente`() {
        // Given: Una categoría existente
        val categorias = repository.getCategorias()
        if (categorias.isNotEmpty()) {
            val categoria = categorias.first()

            // When: Filtramos por esa categoría
            val servicios = repository.getServiciosPorCategoria(categoria)

            // Then: Todos deben ser de esa categoría
            servicios.forEach { servicio ->
                assertEquals("Servicio debe ser de la categoría correcta",
                    categoria, servicio.categoria)
            }
        }
    }


    // PRUEBA 6: Buscar por SKU existente

    @Test
    fun `getServicioPorSku debe retornar servicio si existe`() {
        // Given: Un SKU existente
        val servicios = repository.getServicios()
        if (servicios.isNotEmpty()) {
            val skuExistente = servicios.first().sku

            // When: Buscamos por SKU
            val servicio = repository.getServicioPorSku(skuExistente)

            // Then: Debe encontrarlo
            assertNotNull("Debe encontrar el servicio", servicio)
            assertEquals("SKU debe coincidir", skuExistente, servicio?.sku)
        }
    }


    // PRUEBA 7: Buscar por SKU inexistente

    @Test
    fun `getServicioPorSku debe retornar null si no existe`() {
        // Given: Un SKU que no existe
        val skuInexistente = "SKU_NO_EXISTE_12345"

        // When: Buscamos por SKU
        val servicio = repository.getServicioPorSku(skuInexistente)

        // Then: Debe retornar null
        assertNull("No debe encontrar el servicio", servicio)
    }


    // PRUEBA 8: Servicios tienen SKU único

    @Test
    fun `servicios deben tener SKUs unicos`() {
        // When: Obtenemos servicios
        val servicios = repository.getServicios()

        // Then: Los SKUs deben ser únicos
        val skus = servicios.map { it.sku }
        val skusUnicos = skus.toSet()
        assertEquals("SKUs deben ser únicos", skus.size, skusUnicos.size)
    }

    // PRUEBA 9: Servicios tienen nombre

    @Test
    fun `todos los servicios deben tener nombre`() {
        // When: Obtenemos servicios
        val servicios = repository.getServicios()

        // Then: Todos deben tener nombre
        servicios.forEach { servicio ->
            assertTrue("Servicio debe tener nombre",
                servicio.nombre.isNotEmpty())
        }
    }


    // PRUEBA 10: Servicios tienen precio válido

    @Test
    fun `todos los servicios deben tener precio positivo`() {
        // When: Obtenemos servicios
        val servicios = repository.getServicios()

        // Then: Todos deben tener precio > 0
        servicios.forEach { servicio ->
            assertTrue("Precio debe ser mayor a 0", servicio.precio > 0)
        }
    }
}