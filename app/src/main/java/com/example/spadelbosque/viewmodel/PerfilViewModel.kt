package com.example.spadelbosque.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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
    val id: Long = 0,
    val nombres: String = "",
    val apellidos: String = "",
    val telefono: String = "",
    val email: String = "",
    val region: String = "",
    val comuna: String = "",
    val fechaNacimiento: String = "",
    val cargando: Boolean = false,
    val compras: List<CompraItem> = emptyList(),
    val listaDeComunas: List<String> = listOf("Providencia", "Las Condes", "Santiago", "Ñuñoa", "Viña del Mar", "Valparaiso"),
    val listaDeRegiones: List<String> = listOf("Región Metropolitana", "Región de Valparaíso", "Región de Antofagasta",
        "Región de Atacama", "Región de Coquimbo","Región de O'Higgins", "Región del Maule",  "Región del Biobío")
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

    init {
        cargar()
    }
    fun onComunaSelected(nuevaComuna: String) {
        _ui.update { it.copy(comuna = nuevaComuna) }
    }
    fun onRegionSelected(nuevaRegion: String) {
        _ui.update { it.copy(region= nuevaRegion) }
    }

    //-------------------------CARGAR---------------------
    fun cargar() = viewModelScope.launch {
        _ui.update { it.copy(cargando = true) }

        val sesion = auth.obtenerSesionActiva()
        if (sesion?.id == null) {
            _ui.update { it.copy(cargando = false) }
            return@launch
        }

        val usuarioCompleto = auth.obtenerUsuarioPorId(sesion.id)

        // --- 👇 LÓGICA DE CONVERSIÓN DE FECHA PARA LA UI ---
        val fechaUI: String = usuarioCompleto?.fechaNacimiento?.let { fechaBackend ->
            try {
                // Asume que la fecha del backend viene como 'YYYY-MM-DD' (o con hora)
                val formatoBackend = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val fechaDate = formatoBackend.parse(fechaBackend.substring(0, 10)) // Tomamos solo la parte de la fecha
                val formatoUI = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                formatoUI.format(fechaDate!!)
            } catch (e: Exception) {
                Log.e("PerfilViewModel", "Error al convertir fecha: ", e)
                // Si el formato ya es dd/MM/yyyy o es inválido, lo dejamos como está
                if (fechaBackend.matches(Regex("\\d{2}/\\d{2}/\\d{4}"))) fechaBackend else ""
            }
        } ?: ""

        val comprasMock = ui.value.compras

        _ui.update {
            it.copy(
                id = usuarioCompleto?.id ?: 0,
                nombres = usuarioCompleto?.nombres ?: "",
                apellidos = usuarioCompleto?.apellidos ?: "",
                telefono = usuarioCompleto?.telefono ?: "",
                email = usuarioCompleto?.email ?: "",
                region = usuarioCompleto?.region ?: "",
                comuna = usuarioCompleto?.comuna ?: "",
                fechaNacimiento = fechaUI,
                compras = comprasMock,
                cargando = false
            )
        }
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
    fun onEmail(v: String) = _ui.update { it.copy(email = v) }
    fun onFechaNacimiento(v: String) = _ui.update { it.copy(fechaNacimiento = v) }




    fun guardarCambios(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) = viewModelScope.launch {

        val sesion = auth.obtenerSesionActiva()
        if (sesion?.id == null) {
            onError("Sesión no válida")
            return@launch
        }

        val actualizado = sesion.copy(
            nombres = _ui.value.nombres,
            apellidos = _ui.value.apellidos,
            telefono = _ui.value.telefono,
            region = _ui.value.region,
            comuna = _ui.value.comuna,
            fechaNacimiento = _ui.value.fechaNacimiento

        )

        val resultado = auth.actualizarUsuario(sesion.id, actualizado)

        if (resultado != null) {
            auth.guardarSesion(resultado)
            onSuccess()
        } else {
            onError("Error al actualizar perfil")
        }
    }

}
