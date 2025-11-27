package com.example.spadelbosque.repository

import android.content.Context
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.spadelbosque.data.dataStore
import com.example.spadelbosque.data.remote.RetrofitClient
import com.example.spadelbosque.data.remote.dto.ApiError
import com.example.spadelbosque.data.remote.dto.LoginRequest
import com.example.spadelbosque.data.remote.dto.RegisterRequest
import com.example.spadelbosque.data.remote.dto.UpdateUsuarioRequest
import com.example.spadelbosque.data.remote.dto.toUsuarioLocal
import com.example.spadelbosque.model.Usuario
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import com.google.gson.Gson
class AuthRepositoryImpl(
    private val context: Context
) : AuthRepository {

    private val KEY_SESION = stringPreferencesKey("correo_sesion")
    private val KEY_ID = longPreferencesKey("id_sesion")
    private val api = RetrofitClient.instance
    private val gson = Gson()

    // =========================
    // Registro (API)
    // =========================
    override suspend fun registrarUsuario(usuario: Usuario): Boolean {
        return try {
            val response = api.register(
                RegisterRequest(
                    nombres = usuario.nombres,
                    apellidos = usuario.apellidos,
                    email = usuario.email,
                    password = usuario.password,
                    telefono = usuario.telefono,
                    region = null,  // No existen en el registro
                    comuna = null  // No existen en el registro si en la web
                )
            )

            response.exito
        } catch (e: HttpException) {
            throw RuntimeException(extraerMensajeError(e))
        }
    }

    // =========================
    // Login (API)
    // =========================
    override suspend fun validarCredenciales(correo: String, password: String): Usuario? {
        return try {
            val response = api.login(LoginRequest(correo, password))
            response.toUsuarioLocal()
        } catch (e: HttpException) {
            Log.e("AuthRepo", "Error Error HTTP en login: ${e.code()}", e)
            null
        }
    }

    override suspend fun existeUsuario(correo: String): Boolean {
        return false //el backend valida si existe el usuario o no
    }

    // =========================
    // Sesión local
    // =========================
    override suspend fun guardarSesion(usuario: Usuario) {
        context.dataStore.edit {
            it[KEY_SESION] = usuario.email
            usuario.id?.let{ id ->
                it[KEY_ID] = id
            }
        }
    }

    override suspend fun obtenerSesionActiva(): Usuario? {
        val preferences = context.dataStore.data.first()
        val correo = preferences[KEY_SESION]
        val id = preferences[KEY_ID]
        return if (correo != null && id != null) {
            Usuario(id = id, email = correo)

        } else {
            null
        }
    }

    override suspend fun cerrarSesion() {
        context.dataStore.edit {
            it.remove(KEY_SESION)
            it.remove(KEY_ID)
        }
    }

    override suspend fun haySesionActiva(): Boolean {
        val preferences = context.dataStore.data.first()
        return preferences[KEY_SESION] != null
    }

    // -------------------------
    // PERFIL (luego se conecta a endpoint GET/PUT)
    // -------------------------
    override suspend fun obtenerUsuarioPorId(id: Long): Usuario? {
        return try {
            val res = api.getById(id)
            res.toUsuarioLocal()
        } catch (e: Exception) {
            Log.e("AuthRepo", "Error al obtener usuario: ", e)
            null
        }
    }

    override suspend fun actualizarUsuario(id: Long, usuario: Usuario): Usuario? {
        return try {
            // --- 👇 LÓGICA DE CONVERSIÓN DE FECHA ---
            val fechaBackend: String? = usuario.fechaNacimiento?.let { fechaUI ->
                try {
                    val formatoUI = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val fechaDate = formatoUI.parse(fechaUI)
                    val formatoBackend = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    formatoBackend.format(fechaDate!!)
                } catch (e: Exception) {
                    Log.e("AuthRepo", "Error al convertir fecha: ", e)
                    // Si el formato ya es YYYY-MM-DD o es inválido, devuélve como está o null
                    if (fechaUI.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) fechaUI else null
                }
            }

            val res = api.update(
                id,
                UpdateUsuarioRequest(
                    nombres = usuario.nombres,
                    apellidos = usuario.apellidos,
                    telefono = usuario.telefono,
                    region = usuario.region,
                    comuna = usuario.comuna,
                    fechaNacimiento = fechaBackend
                )
            )
            res.toUsuarioLocal()
        } catch (e: Exception) {
            Log.e("AuthRepo", "Error al actualizar: ", e)
            null
        }
    }


    // -------------------------
    // Helper para leer el {message:"..."} del backend
    // -------------------------
    private fun extraerMensajeError(e: HttpException): String {
        return try {
            val body = e.response()?.errorBody()?.string()
            if (body.isNullOrBlank()) "Error de servidor (${e.code()})"
            else gson.fromJson(body, ApiError::class.java)?.message
                ?: "Error de servidor (${e.code()})"
        } catch (_: Exception) {
            "Error de servidor (${e.code()})"
        }
    }
}
