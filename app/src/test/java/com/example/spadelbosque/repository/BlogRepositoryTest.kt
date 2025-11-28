package com.example.spadelbosque.repository

import org.junit.Test
import org.junit.Assert.*

class BlogRepositoryTest {


    // PRUEBA 1: Get articles retorna lista

    @Test
    fun `getArticles debe retornar lista de articulos`() {
        // When: Obtenemos artículos
        val articulos = BlogRepository.getArticles()

        // Then: Debe retornar lista
        assertNotNull("Lista no debe ser null", articulos)
    }


    // PRUEBA 2: Lista no está vacía

    @Test
    fun `getArticles debe retornar lista no vacia`() {
        // When: Obtenemos artículos
        val articulos = BlogRepository.getArticles()

        // Then: Debe tener artículos
        assertTrue("Debe tener artículos", articulos.isNotEmpty())
    }


    // PRUEBA 3: Artículos tienen título

    @Test
    fun `todos los articulos deben tener titulo`() {
        // When: Obtenemos artículos
        val articulos = BlogRepository.getArticles()

        // Then: Todos deben tener título
        articulos.forEach { articulo ->
            assertTrue("Artículo debe tener título", articulo.title.isNotEmpty())
        }
    }


    // PRUEBA 4: Artículos tienen autor

    @Test
    fun `todos los articulos deben tener autor`() {
        // When: Obtenemos artículos
        val articulos = BlogRepository.getArticles()

        // Then: Todos deben tener autor
        articulos.forEach { articulo ->
            assertTrue("Artículo debe tener autor", articulo.author.isNotEmpty())
        }
    }


    // PRUEBA 5: Artículos tienen fecha

    @Test
    fun `todos los articulos deben tener fecha`() {
        // When: Obtenemos artículos
        val articulos = BlogRepository.getArticles()

        // Then: Todos deben tener fecha
        articulos.forEach { articulo ->
            assertTrue("Artículo debe tener fecha", articulo.date.isNotEmpty())
        }
    }


    // PRUEBA 6: Artículos tienen IDs únicos

    @Test
    fun `articulos deben tener IDs unicos`() {
        // When: Obtenemos artículos
        val articulos = BlogRepository.getArticles()

        // Then: Los IDs deben ser únicos
        val ids = articulos.map { it.id }
        val idsUnicos = ids.toSet()
        assertEquals("IDs deben ser únicos", ids.size, idsUnicos.size)
    }


    // PRUEBA 7: Cantidad esperada de artículos

    @Test
    fun `debe retornar 3 articulos`() {
        // When: Obtenemos artículos
        val articulos = BlogRepository.getArticles()

        // Then: Debe haber 3 artículos
        assertEquals("Debe haber 3 artículos", 3, articulos.size)
    }
}