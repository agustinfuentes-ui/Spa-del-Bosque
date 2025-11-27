package com.example.spadelbosque.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

class BlogViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: BlogViewModel

    @Before
    fun setup() {
        viewModel = BlogViewModel()
    }

    @Test
    fun `getArticles debe retornar lista de articulos`() {
        // When: Obtenemos artículos
        val articulos = viewModel.getArticles()

        // Then: Debe retornar una lista
        assertNotNull("Lista no debe ser null", articulos)
    }

    @Test
    fun `getArticles debe retornar lista no vacia`() {
        // When: Obtenemos artículos
        val articulos = viewModel.getArticles()

        // Then: Debe tener artículos
        assertTrue("Debe tener artículos", articulos.isNotEmpty())
    }
}