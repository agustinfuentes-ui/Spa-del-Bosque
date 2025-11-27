package com.example.spadelbosque.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

class ContactoViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ContactoViewModel

    @Before
    fun setup() {
        viewModel = ContactoViewModel()
    }

    @Test
    fun `estado inicial debe estar vacio`() {
        // When: Obtenemos estado inicial
        val estado = viewModel.estado.value

        // Then: Todo debe estar vacío
        assertTrue("Nombre debe estar vacío", estado.nombre.isBlank())
        assertTrue("Correo debe estar vacío", estado.correo.isBlank())
        assertTrue("Asunto debe estar vacío", estado.asunto.isBlank())
        assertTrue("Mensaje debe estar vacío", estado.mensaje.isBlank())
    }

    @Test
    fun `cambiar nombre valido debe actualizar estado`() {
        // When: Cambiamos nombre válido
        viewModel.onNombreChange("Juan Pérez")

        // Then: Debe actualizarse sin errores
        assertEquals("Juan Pérez", viewModel.estado.value.nombre)
        assertNull("No debe tener error", viewModel.estado.value.errores.nombre)
    }

    @Test
    fun `nombre con numeros debe generar error`() {
        // When: Ingresamos nombre con números
        viewModel.onNombreChange("Juan123")

        // Then: Debe tener error
        assertNotNull("Debe tener error", viewModel.estado.value.errores.nombre)
    }

    @Test
    fun `correo con dominio valido no debe tener error`() {
        // When: Ingresamos correo válido
        viewModel.onCorreoChange("test@gmail.com")

        // Then: No debe tener error
        assertNull("No debe tener error", viewModel.estado.value.errores.correo)
    }

    @Test
    fun `correo con dominio invalido debe tener error`() {
        // When: Ingresamos correo con dominio no permitido
        viewModel.onCorreoChange("test@invalido.com")

        // Then: Debe tener error
        assertNotNull("Debe tener error", viewModel.estado.value.errores.correo)
    }

    @Test
    fun `cambiar asunto debe actualizar estado`() {
        // When: Seleccionamos asunto
        viewModel.onAsuntoChange("Consulta")

        // Then: Debe actualizarse
        assertEquals("Consulta", viewModel.estado.value.asunto)
    }

    @Test
    fun `mensaje largo debe truncarse a 500 caracteres`() {
        // Given: Mensaje muy largo
        val mensajeLargo = "a".repeat(600)

        // When: Intentamos establecerlo
        viewModel.onMensajeChange(mensajeLargo)

        // Then: No debe cambiar (se mantiene vacío)
        assertTrue("Mensaje debe mantenerse vacío o corto",
            viewModel.estado.value.mensaje.length <= 500)
    }

    @Test
    fun `validar formulario vacio debe retornar false`() {
        // Given: Formulario vacío

        // When: Validamos
        val esValido = viewModel.validaFormulario()

        // Then: No debe ser válido
        assertFalse("Formulario vacío no debe ser válido", esValido)
    }

    @Test
    fun `validar formulario completo debe retornar true`() {
        // Given: Formulario completo
        viewModel.onNombreChange("Juan Pérez")
        viewModel.onCorreoChange("test@gmail.com")
        viewModel.onAsuntoChange("Consulta")
        viewModel.onMensajeChange("Este es un mensaje de prueba")

        // When: Validamos
        val esValido = viewModel.validaFormulario()

        // Then: Debe ser válido
        assertTrue("Formulario completo debe ser válido", esValido)
    }

    @Test
    fun `limpiar formulario debe resetear estado`() {
        // Given: Formulario con datos
        viewModel.onNombreChange("Juan")
        viewModel.onCorreoChange("test@gmail.com")
        viewModel.onMensajeChange("Mensaje")

        // When: Limpiamos
        viewModel.limpiarFormulario()

        // Then: Todo debe estar vacío
        assertTrue("Nombre debe estar vacío", viewModel.estado.value.nombre.isBlank())
        assertTrue("Correo debe estar vacío", viewModel.estado.value.correo.isBlank())
        assertTrue("Mensaje debe estar vacío", viewModel.estado.value.mensaje.isBlank())
    }

    @Test
    fun `asuntos disponibles debe tener opciones`() {
        // When: Obtenemos asuntos
        val asuntos = viewModel.asuntosDisponibles

        // Then: Debe haber opciones
        assertTrue("Debe tener asuntos disponibles", asuntos.isNotEmpty())
        assertTrue("Debe contener Consulta", asuntos.contains("Consulta"))
    }
}