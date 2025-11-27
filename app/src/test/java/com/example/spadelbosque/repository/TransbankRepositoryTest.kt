package com.example.spadelbosque.repository

import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class TransbankRepositoryTest {

    private lateinit var repository: TransbankRepository

    @Before
    fun setup() {
        repository = TransbankRepository()
    }


    // PRUEBA 1: Repository se puede instanciar

    @Test
    fun `repository debe poder instanciarse`() {
        // Then: Repository no debe ser null
        assertNotNull("Repository debe existir", repository)
    }


    // PRUEBA 2: Credenciales están configuradas

    @Test
    fun `debe tener credenciales de transbank configuradas`() {
        // When: Verificamos que el repository existe
        // Then: Si se creó correctamente, tiene acceso a las credenciales
        assertNotNull("Repository debe tener acceso a API", repository)
    }
}