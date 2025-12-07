/*package com.example.spadelbosque.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.spadelbosque.model.Usuario
import com.example.spadelbosque.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
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
class AuthViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var mockRepository: AuthRepository
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mockk(relaxed = true)

        // Mock para obtener sesión inicial
        coEvery { mockRepository.obtenerSesionActiva() } returns null

        viewModel = AuthViewModel(mockRepository)
        testDispatcher.scheduler.advanceUntilIdle()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    // LOGIN - PRUEBAS


    @Test
    fun `cambiar correo login debe actualizar estado`() {
        // When: Cambiamos el correo
        viewModel.onCorreoLoginChange("test@duoc.cl")

        // Then: El estado debe tener el nuevo correo
        assertEquals("test@duoc.cl", viewModel.loginState.value.correo)
    }

    @Test
    fun `cambiar password login debe actualizar estado`() {
        // When: Cambiamos el password
        viewModel.onPasswordLoginChange("123456")

        // Then: El estado debe tener el nuevo password
        assertEquals("123456", viewModel.loginState.value.password)
    }

    @Test
    fun `toggle mostrar password debe cambiar visibilidad`() {
        // Given: Password oculto
        val inicial = viewModel.loginState.value.mostrarPassword

        // When: Toggleamos
        viewModel.toggleMostrarPassword()

        // Then: Debe cambiar
        assertEquals(!inicial, viewModel.loginState.value.mostrarPassword)
    }

    @Test
    fun `validar login con campos vacios debe retornar false`() {
        // Given: Campos vacíos (estado inicial)

        // When: Validamos
        val esValido = viewModel.validarLogin()

        // Then: No debe ser válido
        assertFalse("Login vacío no debe ser válido", esValido)
        assertNotNull("Debe tener error en correo",
            viewModel.loginState.value.errores.correo)
    }

    @Test
    fun `validar login con correo invalido debe retornar false`() {
        // Given: Correo sin @
        viewModel.onCorreoLoginChange("correoinvalido")
        viewModel.onPasswordLoginChange("123456")

        // When: Validamos
        val esValido = viewModel.validarLogin()

        // Then: No debe ser válido
        assertFalse("Correo inválido no debe ser válido", esValido)
        assertNotNull("Debe tener error en correo",
            viewModel.loginState.value.errores.correo)
    }

    @Test
    fun `validar login con password corto debe retornar false`() {
        // Given: Password muy corto
        viewModel.onCorreoLoginChange("test@duoc.cl")
        viewModel.onPasswordLoginChange("123")

        // When: Validamos
        val esValido = viewModel.validarLogin()

        // Then: No debe ser válido
        assertFalse("Password corto no debe ser válido", esValido)
        assertNotNull("Debe tener error en password",
            viewModel.loginState.value.errores.password)
    }

    @Test
    fun `validar login con datos correctos debe retornar true`() {
        // Given: Datos válidos
        viewModel.onCorreoLoginChange("test@duoc.cl")
        viewModel.onPasswordLoginChange("123456")

        // When: Validamos
        val esValido = viewModel.validarLogin()

        // Then: Debe ser válido
        assertTrue("Login válido debe retornar true", esValido)
        assertNull("No debe tener errores",
            viewModel.loginState.value.errores.correo)
        assertNull("No debe tener errores",
            viewModel.loginState.value.errores.password)
    }

    @Test
    fun `login exitoso debe llamar callback onSuccess`() = runTest {
        // Given: Credenciales válidas y usuario existente
        val usuario = Usuario(
            id = "1", nombres = "Juan", apellidos = "Pérez",
            correo = "test@duoc.cl", password = "123456", telefono = "987654321"
        )
        coEvery { mockRepository.validarCredenciales(any(), any()) } returns usuario
        coEvery { mockRepository.guardarSesion(any()) } returns Unit

        viewModel.onCorreoLoginChange("test@duoc.cl")
        viewModel.onPasswordLoginChange("123456")

        var successCalled = false

        // When: Intentamos login
        viewModel.intentarLogin(
            onSuccess = { successCalled = true },
            onError = {}
        )
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: Debe llamar onSuccess
        assertTrue("Debe llamar onSuccess", successCalled)
        assertEquals("Debe actualizar sesión", usuario, viewModel.sesionState.value)
    }

    @Test
    fun `login fallido debe llamar callback onError`() = runTest {
        // Given: Credenciales inválidas
        coEvery { mockRepository.validarCredenciales(any(), any()) } returns null

        viewModel.onCorreoLoginChange("test@duoc.cl")
        viewModel.onPasswordLoginChange("wrongpass")

        var errorCalled = false

        // When: Intentamos login
        viewModel.intentarLogin(
            onSuccess = {},
            onError = { errorCalled = true }
        )
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: Debe llamar onError
        assertTrue("Debe llamar onError", errorCalled)
    }


    // REGISTRO - PRUEBAS


    @Test
    fun `cambiar nombres registro debe actualizar estado`() {
        // When: Cambiamos nombres
        viewModel.onNombresChange("Juan")

        // Then: Debe actualizarse
        assertEquals("Juan", viewModel.registroState.value.nombres)
    }

    @Test
    fun `cambiar telefono debe filtrar solo numeros`() {
        // When: Ingresamos teléfono con letras
        viewModel.onTelefonoChange("123abc456")

        // Then: Solo debe guardar números
        assertEquals("123456", viewModel.registroState.value.telefono)
    }

    @Test
    fun `validar registro con campos vacios debe retornar false`() {
        // Given: Campos vacíos

        // When: Validamos
        val esValido = viewModel.validarRegistro()

        // Then: No debe ser válido
        assertFalse("Registro vacío no debe ser válido", esValido)
    }

    @Test
    fun `validar registro con passwords diferentes debe retornar false`() {
        // Given: Datos completos pero passwords diferentes
        viewModel.onNombresChange("Juan")
        viewModel.onApellidosChange("Pérez")
        viewModel.onCorreoRegistroChange("test@duoc.cl")
        viewModel.onPasswordRegistroChange("123456")
        viewModel.onConfirmarPasswordChange("654321")
        viewModel.onTelefonoChange("987654321")

        // When: Validamos
        val esValido = viewModel.validarRegistro()

        // Then: No debe ser válido
        assertFalse("Passwords diferentes no deben ser válidos", esValido)
        assertNotNull("Debe tener error en confirmar password",
            viewModel.registroState.value.errores.confirmarPassword)
    }

    @Test
    fun `validar registro con datos correctos debe retornar true`() {
        // Given: Todos los datos válidos
        viewModel.onNombresChange("Juan")
        viewModel.onApellidosChange("Pérez")
        viewModel.onCorreoRegistroChange("test@duoc.cl")
        viewModel.onPasswordRegistroChange("123456")
        viewModel.onConfirmarPasswordChange("123456")
        viewModel.onTelefonoChange("987654321")

        // When: Validamos
        val esValido = viewModel.validarRegistro()

        // Then: Debe ser válido
        assertTrue("Registro válido debe retornar true", esValido)
    }

    @Test
    fun `registro exitoso debe llamar onSuccess`() = runTest {
        // Given: Datos válidos y usuario no existe
        coEvery { mockRepository.existeUsuario(any()) } returns false
        coEvery { mockRepository.registrarUsuario(any()) } returns true

        viewModel.onNombresChange("Juan")
        viewModel.onApellidosChange("Pérez")
        viewModel.onCorreoRegistroChange("test@duoc.cl")
        viewModel.onPasswordRegistroChange("123456")
        viewModel.onConfirmarPasswordChange("123456")
        viewModel.onTelefonoChange("987654321")

        var successCalled = false

        // When: Intentamos registro
        viewModel.intentarRegistro(
            onSuccess = { successCalled = true },
            onError = {}
        )
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: Debe llamar onSuccess
        assertTrue("Debe llamar onSuccess", successCalled)
    }

    @Test
    fun `registro con correo existente debe llamar onError`() = runTest {
        // Given: Usuario ya existe
        coEvery { mockRepository.existeUsuario(any()) } returns true

        viewModel.onNombresChange("Juan")
        viewModel.onApellidosChange("Pérez")
        viewModel.onCorreoRegistroChange("existente@duoc.cl")
        viewModel.onPasswordRegistroChange("123456")
        viewModel.onConfirmarPasswordChange("123456")
        viewModel.onTelefonoChange("987654321")

        var errorCalled = false

        // When: Intentamos registro
        viewModel.intentarRegistro(
            onSuccess = {},
            onError = { errorCalled = true }
        )
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: Debe llamar onError
        assertTrue("Debe llamar onError", errorCalled)
    }


    // SESIÓN - PRUEBAS


    @Test
    fun `cerrar sesion debe limpiar estado y llamar callback`() = runTest {
        // Given: Usuario logueado
        val usuario = Usuario(
            id = "1", nombres = "Juan", apellidos = "Pérez",
            correo = "test@duoc.cl", password = "123456", telefono = "987654321"
        )
        coEvery { mockRepository.obtenerSesionActiva() } returns usuario

        val vm = AuthViewModel(mockRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        var callbackCalled = false

        // When: Cerramos sesión
        vm.cerrarSesion { callbackCalled = true }
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: Debe limpiar sesión y llamar callback
        assertNull("Sesión debe ser null", vm.sesionState.value)
        assertTrue("Debe llamar callback", callbackCalled)
        coVerify { mockRepository.cerrarSesion() }
    }
}

*/
