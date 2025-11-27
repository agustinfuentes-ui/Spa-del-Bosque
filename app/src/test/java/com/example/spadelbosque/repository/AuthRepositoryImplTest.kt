package com.example.spadelbosque.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.spadelbosque.data.dataStore
import com.example.spadelbosque.data.local.AppDatabase
import com.example.spadelbosque.data.local.UsuarioDao
import com.example.spadelbosque.data.local.UsuarioEntity
import com.example.spadelbosque.model.Usuario
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*


class AuthRepositoryImplTest {

    private lateinit var mockContext: Context
    private lateinit var mockDatabase: AppDatabase
    private lateinit var mockDao: UsuarioDao
    private lateinit var mockDataStore: DataStore<Preferences>
    private lateinit var repository: AuthRepositoryImpl


    @Before
    fun setup() {
        mockContext = mockk(relaxed = true)
        mockDatabase = mockk(relaxed = true)
        mockDao = mockk(relaxed = true)
        mockDataStore = mockk(relaxed = true)

        every { mockDatabase.usuarioDao() } returns mockDao
        
        // Mockear la extension property usando mockkStatic
        mockkStatic("com.example.spadelbosque.data.DataStoreKt")
        every { mockContext.dataStore } returns mockDataStore

        // Mock preferences vacías por defecto
        val emptyPrefs = mockk<Preferences>(relaxed = true)
        every { emptyPrefs.get<String>(any()) } returns null
        every { mockDataStore.data } returns flowOf(emptyPrefs)

        repository = AuthRepositoryImpl(mockContext, mockDatabase)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }


    // ==================== TESTS DE REGISTRO ====================

    @Test
    fun `registrar usuario nuevo debe retornar true`() = runTest {
        // Given: Usuario no existe
        coEvery { mockDao.existeUsuario(any()) } returns false
        coEvery { mockDao.insertar(any()) } returns Unit

        val usuario = Usuario(
            id = "0",
            nombres = "Juan",
            apellidos = "Pérez",
            correo = "juan@duoc.cl",
            password = "123456",
            telefono = "987654321"
        )

        // When: Registramos
        val resultado = repository.registrarUsuario(usuario)

        // Then: Debe retornar true
        assertTrue("Registro debe ser exitoso", resultado)
        coVerify { mockDao.insertar(any()) }
    }

    @Test
    fun `registrar usuario existente debe retornar false`() = runTest {
        // Given: Usuario ya existe
        coEvery { mockDao.existeUsuario(any()) } returns true

        val usuario = Usuario(
            id = "0",
            nombres = "Juan",
            apellidos = "Pérez",
            correo = "existente@duoc.cl",
            password = "123456",
            telefono = "987654321"
        )

        // When: Intentamos registrar
        val resultado = repository.registrarUsuario(usuario)

        // Then: Debe retornar false
        assertFalse("No debe registrar usuario existente", resultado)
        coVerify(exactly = 0) { mockDao.insertar(any()) }
    }


    // ==================== TESTS DE VALIDACIÓN ====================

    @Test
    fun `validar credenciales correctas debe retornar usuario`() = runTest {
        // Given: Credenciales válidas
        val usuarioEntity = UsuarioEntity(
            id = "1",
            nombres = "Juan",
            apellidos = "Pérez",
            correo = "juan@duoc.cl",
            password = "123456",
            telefono = "987654321"
        )
        coEvery { mockDao.validarCredenciales(any(), any()) } returns usuarioEntity

        // When: Validamos
        val resultado = repository.validarCredenciales("juan@duoc.cl", "123456")

        // Then: Debe retornar usuario
        assertNotNull("Debe retornar usuario", resultado)
        assertEquals("juan@duoc.cl", resultado?.correo)
        assertEquals("Juan", resultado?.nombres)
    }

    @Test
    fun `validar credenciales incorrectas debe retornar null`() = runTest {
        // Given: Credenciales inválidas
        coEvery { mockDao.validarCredenciales(any(), any()) } returns null

        // When: Validamos
        val resultado = repository.validarCredenciales("juan@duoc.cl", "wrong")

        // Then: Debe retornar null
        assertNull("Debe retornar null", resultado)
    }

    @Test
    fun `validar con correo vacio debe retornar null`() = runTest {
        // Given: Correo vacío
        coEvery { mockDao.validarCredenciales("", any()) } returns null

        // When: Validamos
        val resultado = repository.validarCredenciales("", "123456")

        // Then: Debe retornar null
        assertNull("Debe retornar null con correo vacío", resultado)
    }


    // ==================== TESTS DE EXISTENCIA ====================

    @Test
    fun `existe usuario debe retornar true si existe`() = runTest {
        // Given: Usuario existe
        coEvery { mockDao.existeUsuario(any()) } returns true

        // When: Verificamos
        val existe = repository.existeUsuario("juan@duoc.cl")

        // Then: Debe retornar true
        assertTrue("Usuario debe existir", existe)
    }

    @Test
    fun `existe usuario debe retornar false si no existe`() = runTest {
        // Given: Usuario no existe
        coEvery { mockDao.existeUsuario(any()) } returns false

        // When: Verificamos
        val existe = repository.existeUsuario("noexiste@duoc.cl")

        // Then: Debe retornar false
        assertFalse("Usuario no debe existir", existe)
    }


    // ==================== TESTS DE SESIÓN ====================

    @Test
    fun `guardar sesion debe ejecutarse sin errores`() = runTest {
        // Given: Usuario válido
        val usuario = Usuario(
            id = "1",
            nombres = "Juan",
            apellidos = "Pérez",
            correo = "juan@duoc.cl",
            password = "123456",
            telefono = "987654321"
        )

        // When: Guardamos sesión (no lanza excepción)
        try {
            repository.guardarSesion(usuario)
            // Then: Debe ejecutarse sin errores
            assertTrue("Guardar sesión debe completarse", true)
        } catch (e: Exception) {
            fail("No debería lanzar excepción: ${e.message}")
        }
    }

    @Test
    fun `obtener sesion activa con usuario logueado debe retornar usuario`() = runTest {
        // Given: Preferences con correo guardado
        val prefs = mockk<Preferences>(relaxed = true)
        val key = stringPreferencesKey("correo_sesion")
        every { prefs[key] } returns "juan@duoc.cl"
        every { mockDataStore.data } returns flowOf(prefs)

        val usuarioEntity = UsuarioEntity(
            id = "1",
            nombres = "Juan",
            apellidos = "Pérez",
            correo = "juan@duoc.cl",
            password = "123456",
            telefono = "987654321"
        )
        coEvery { mockDao.obtenerPorCorreo("juan@duoc.cl") } returns usuarioEntity

        // When: Obtenemos sesión activa
        val usuario = repository.obtenerSesionActiva()

        // Then: Debe retornar el usuario
        assertNotNull("Debe retornar usuario", usuario)
        assertEquals("juan@duoc.cl", usuario?.correo)
        coVerify { mockDao.obtenerPorCorreo("juan@duoc.cl") }
    }

    @Test
    fun `obtener sesion activa sin usuario logueado debe retornar null`() = runTest {
        // Given: Ya configurado en setup (preferences vacías)

        // When: Obtenemos sesión activa
        val usuario = repository.obtenerSesionActiva()

        // Then: Debe retornar null
        assertNull("No debe haber usuario activo", usuario)
    }

    @Test
    fun `cerrar sesion debe ejecutarse sin errores`() = runTest {
        // When: Cerramos sesión (no lanza excepción)
        try {
            repository.cerrarSesion()
            // Then: Debe ejecutarse sin errores
            assertTrue("Cerrar sesión debe completarse", true)
        } catch (e: Exception) {
            fail("No debería lanzar excepción: ${e.message}")
        }
    }

    @Test
    fun `hay sesion activa debe retornar true si hay sesion`() = runTest {
        // Given: Preferences con correo
        val prefs = mockk<Preferences>(relaxed = true)
        val key = stringPreferencesKey("correo_sesion")
        every { prefs[key] } returns "juan@duoc.cl"
        every { mockDataStore.data } returns flowOf(prefs)

        // When: Verificamos
        val haySesion = repository.haySesionActiva()

        // Then: Debe retornar true
        assertTrue("Debe haber sesión activa", haySesion)
    }

    @Test
    fun `hay sesion activa debe retornar false si no hay sesion`() = runTest {
        // Given: Ya configurado en setup (preferences vacías)

        // When: Verificamos
        val haySesion = repository.haySesionActiva()

        // Then: Debe retornar false
        assertFalse("No debe haber sesión activa", haySesion)
    }


    // ==================== TESTS DE PERFIL ====================

    @Test
    fun `obtener perfil actual debe retornar usuario si hay sesion`() = runTest {
        // Given: Sesión activa
        val prefs = mockk<Preferences>(relaxed = true)
        val key = stringPreferencesKey("correo_sesion")
        every { prefs[key] } returns "juan@duoc.cl"
        every { mockDataStore.data } returns flowOf(prefs)

        val usuarioEntity = UsuarioEntity(
            id = "1",
            nombres = "Juan",
            apellidos = "Pérez",
            correo = "juan@duoc.cl",
            password = "123456",
            telefono = "987654321"
        )
        coEvery { mockDao.obtenerPorCorreo("juan@duoc.cl") } returns usuarioEntity

        // When: Obtenemos perfil
        val perfil = repository.obtenerPerfilActual()

        // Then: Debe retornar el usuario
        assertNotNull("Debe retornar perfil", perfil)
        assertEquals("juan@duoc.cl", perfil?.correo)
    }

    @Test
    fun `obtener perfil actual sin sesion debe retornar null`() = runTest {
        // Given: Sin sesión (configurado en setup)

        // When: Obtenemos perfil
        val perfil = repository.obtenerPerfilActual()

        // Then: Debe retornar null
        assertNull("No debe haber perfil sin sesión", perfil)
    }

    @Test
    fun `actualizar perfil con datos validos debe retornar true`() = runTest {
        // Given: Actualización exitosa
        coEvery { mockDao.actualizarPerfil(any(), any(), any(), any(), any()) } returns 1

        val usuario = Usuario(
            id = "1",
            nombres = "Juan Actualizado",
            apellidos = "Pérez",
            correo = "juan@duoc.cl",
            password = "123456",
            telefono = "912345678",
            fotoUri = "content://photo.jpg"
        )

        // When: Actualizamos perfil
        val resultado = repository.actualizarPerfil(usuario)

        // Then: Debe retornar true
        assertTrue("Actualización debe ser exitosa", resultado)
        coVerify { 
            mockDao.actualizarPerfil(
                id = "1",
                nombres = "Juan Actualizado",
                apellidos = "Pérez",
                telefono = "912345678",
                fotoUri = "content://photo.jpg"
            )
        }
    }

    @Test
    fun `actualizar perfil sin afectar filas debe retornar false`() = runTest {
        // Given: No se actualizó ninguna fila (usuario no existe)
        coEvery { mockDao.actualizarPerfil(any(), any(), any(), any(), any()) } returns 0

        val usuario = Usuario(
            id = "999",
            nombres = "Inexistente",
            apellidos = "Usuario",
            correo = "noexiste@duoc.cl",
            password = "123456",
            telefono = "987654321"
        )

        // When: Actualizamos perfil
        val resultado = repository.actualizarPerfil(usuario)

        // Then: Debe retornar false
        assertFalse("Actualización no debe ser exitosa", resultado)
    }

    @Test
    fun `actualizar perfil sin foto debe funcionar correctamente`() = runTest {
        // Given: Actualización sin foto
        coEvery { mockDao.actualizarPerfil(any(), any(), any(), any(), any()) } returns 1

        val usuario = Usuario(
            id = "1",
            nombres = "Juan",
            apellidos = "Pérez",
            correo = "juan@duoc.cl",
            password = "123456",
            telefono = "987654321",
            fotoUri = null
        )

        // When: Actualizamos perfil
        val resultado = repository.actualizarPerfil(usuario)

        // Then: Debe retornar true
        assertTrue("Actualización sin foto debe ser exitosa", resultado)
        coVerify { 
            mockDao.actualizarPerfil(
                id = "1",
                nombres = "Juan",
                apellidos = "Pérez",
                telefono = "987654321",
                fotoUri = null
            )
        }
    }
}
