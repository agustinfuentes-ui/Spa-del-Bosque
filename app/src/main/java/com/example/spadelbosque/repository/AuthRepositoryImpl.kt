package com.example.spadelbosque.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.spadelbosque.data.dataStore
import com.example.spadelbosque.data.local.AppDatabase
import com.example.spadelbosque.data.local.toEntity
import com.example.spadelbosque.data.local.toUsuario
import com.example.spadelbosque.model.Usuario
import kotlinx.coroutines.flow.first

class AuthRepositoryImpl(
    private val context: Context,
    private val db: AppDatabase
) : AuthRepository {

    private val KEY_SESION = stringPreferencesKey("correo_sesion")

    override suspend fun registrarUsuario(usuario: Usuario): Boolean {
        if (existeUsuario(usuario.correo)) return false
        // CORRECCIÓN: Convertir el modelo a entidad antes de insertar
        db.usuarioDao().insertar(usuario.toEntity())
        return true
    }

    override suspend fun validarCredenciales(correo: String, password: String): Usuario? {
        // CORRECCIÓN: Usar la función correcta del DAO y convertir la entidad a modelo
        val usuarioEntity = db.usuarioDao().validarCredenciales(correo, password)
        return usuarioEntity?.toUsuario()
    }

    override suspend fun existeUsuario(correo: String): Boolean {
        // CORRECCIÓN: Usar la función correcta del DAO
        return db.usuarioDao().existeUsuario(correo)
    }

    override suspend fun guardarSesion(usuario: Usuario) {
        context.dataStore.edit {
            it[KEY_SESION] = usuario.correo
        }
    }

    override suspend fun obtenerSesionActiva(): Usuario? {
        val preferences = context.dataStore.data.first()
        val correo = preferences[KEY_SESION]
        return if (correo != null) {
            // CORRECCIÓN: Usar la función correcta y convertir la entidad a modelo
            db.usuarioDao().obtenerPorCorreo(correo)?.toUsuario()
        } else {
            null
        }
    }

    override suspend fun cerrarSesion() {
        context.dataStore.edit {
            it.remove(KEY_SESION)
        }
    }

    override suspend fun haySesionActiva(): Boolean {
        val preferences = context.dataStore.data.first()
        return preferences[KEY_SESION] != null
    }

    override suspend fun obtenerPerfilActual(): Usuario? {
        return obtenerSesionActiva()
    }

    override suspend fun actualizarPerfil(usuario: Usuario): Boolean {
        // CORRECCIÓN: Usar la función del DAO que actualiza y pasar los parámetros correctos
        val rowsAffected = db.usuarioDao().actualizarPerfil(
            id = usuario.id,
            nombres = usuario.nombres,
            apellidos = usuario.apellidos,
            telefono = usuario.telefono,
            fotoUri = usuario.fotoUri
        )
        return rowsAffected > 0
    }
}
