package com.example.spadelbosque.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.spadelbosque.model.Usuario
import com.example.spadelbosque.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// --- Modelo para el historial de compras ---
data class CompraItem(
    val titulo: String,
    val sku: String,
    val cantidad: Int,
    val precio: Int,
    val fechaStr: String,               // <-- esto es lo que usas en la UI
) {
    val subtotal: Int get() = cantidad * precio
    fun subtotalCLP(): String =
        NumberFormat.getCurrencyInstance(Locale("es", "CL"))
            .apply { maximumFractionDigits = 0 }
            .format(subtotal)
}

// --- UI State del perfil ---
data class PerfilUi(
    val photoUri: String? = null,
    val nombres: String = "",
    val apellidos: String = "",
    val telefono: String = "",
    val correo: String = "",
    val cargando: Boolean = false,
    val compras: List<CompraItem> = emptyList()
) {
    val nombreCompleto: String
        get() = listOf(nombres, apellidos).joinToString(" ").trim()

    val totalComprasCLP: String
        get() = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
            .apply { maximumFractionDigits = 0 }
            .format(compras.sumOf { it.subtotal })
}

class PerfilViewModel(
    app: Application,
    private val auth: AuthRepository
) : AndroidViewModel(app) {

    private val prefs = app.getSharedPreferences("spa_profile", 0)

    private val _ui = MutableStateFlow(PerfilUi())
    val ui: StateFlow<PerfilUi> = _ui.asStateFlow()

    fun cargar() = viewModelScope.launch {
        _ui.update { it.copy(cargando = true) }

        val u = auth.obtenerSesionActiva()
        val photo = prefs.getString("photo_uri", null)

        // Fechas amigables usando API 24 (SimpleDateFormat + Date)
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "CL"))
        val ahora = sdf.format(Date())
        val ayer = sdf.format(Date(System.currentTimeMillis() - 24L * 60 * 60 * 1000))

        // Mock de compras (puedes poblar con datos reales más adelante)
        val compras = listOf(
            CompraItem(
                sku = "RELAX30",
                titulo = "Masaje de Relajación 30 min",
                cantidad = 1,
                precio = 19990,
                fechaStr = ahora
            ),
            CompraItem(
                sku = "RELAX40",
                titulo = "Masaje de Relajación 40 min",
                cantidad = 1,
                precio = 24990,
                fechaStr = ayer
            )
        )

        _ui.value = PerfilUi(
            photoUri = photo,
            nombres = u?.nombres.orEmpty(),
            apellidos = u?.apellidos.orEmpty(),
            telefono = u?.telefono.orEmpty(),
            correo = u?.correo.orEmpty(),
            compras = compras,
            cargando = false
        )
    }

    fun guardarFoto(uri: String?) {
        prefs.edit().putString("photo_uri", uri).apply()
        _ui.update { it.copy(photoUri = uri) }
    }

    fun setPhoto(uri: Uri?) {
        _ui.update { it.copy(photoUri = uri?.toString()) }
        prefs.edit { putString("photo_uri", uri?.toString()) }
    }

    fun setPhotoFromBitmap(bitmap: Bitmap) {
        val file = File(getApplication<Application>().cacheDir,
            "profile_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
        }
        val uri = Uri.fromFile(file)
        prefs.edit { putString("photo_uri", uri.toString()) }
        _ui.update { it.copy(photoUri = uri.toString()) }

    }

    fun onNombres(v: String) = _ui.update { it.copy(nombres = v) }
    fun onApellidos(v: String) = _ui.update { it.copy(apellidos = v) }
    fun onTelefono(v: String) = _ui.update { it.copy(telefono = v) }

    fun guardarCambios() = viewModelScope.launch {
        val current = auth.obtenerSesionActiva()
        if (current != null) {
            val actualizado: Usuario = current.copy(
                nombres = _ui.value.nombres,
                apellidos = _ui.value.apellidos,
                telefono = _ui.value.telefono
            )
            auth.guardarSesion(actualizado) // persiste sesión rápida
        }
        // persistencia adicional ligera
        prefs.edit {
            putString("perfil_nombres", _ui.value.nombres)
            putString("perfil_apellidos", _ui.value.apellidos)
            putString("perfil_tel", _ui.value.telefono)
        }
    }
}
