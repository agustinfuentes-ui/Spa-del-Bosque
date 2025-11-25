package com.example.spadelbosque.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spadelbosque.model.transbank.EstadoTransbank
import com.example.spadelbosque.model.transbank.TransbankCommitResponse
import com.example.spadelbosque.model.transbank.TransbankCreateResponse
import com.example.spadelbosque.repository.TransbankRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class PagoTransbankViewModel : ViewModel() {
    private val repository = TransbankRepository()

    private val _estadoPago = MutableStateFlow<EstadoTransbank>(EstadoTransbank.INICIANDO)
    val estadoPago = _estadoPago.asStateFlow()

    private val _transaccionData = MutableStateFlow<TransbankCreateResponse?>(null)
    val transaccionData = _transaccionData.asStateFlow()

    private val _resultado = MutableStateFlow<TransbankCommitResponse?>(null)
    val resultado = _resultado.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun iniciarPago(monto: Int) {
        viewModelScope.launch {
            _estadoPago.value = EstadoTransbank.INICIANDO
            _error.value = null

            val buyOrder = "ORD-${System.currentTimeMillis()}"
            val sessionId = UUID.randomUUID().toString()
            // URL de retorno
            val returnUrl = "https://ayuda.transbank.cl/documents/433363/1070414/CODIGOBOLETA.png/b2a14b64-f38d-267b-ab6c-80e7c6a76cee?version=1.0&t=1640864483976&imagePreview=1"

            android.util.Log.d("PagoVM", " Iniciando pago:")
            android.util.Log.d("PagoVM", "   Monto: $monto")
            android.util.Log.d("PagoVM", "   Buy Order: $buyOrder")
            android.util.Log.d("PagoVM", "   Return URL: $returnUrl")

            repository.crearTransaccion(
                buyOrder = buyOrder,
                sessionId = sessionId,
                amount = monto,
                returnUrl = returnUrl
            ).onSuccess { response ->
                android.util.Log.d("PagoVM", "Token recibido: ${response.token}")
                android.util.Log.d("PagoVM", "URL Transbank: ${response.url}")
                _transaccionData.value = response
                _estadoPago.value = EstadoTransbank.EN_PROCESO
            }.onFailure { exception ->
                android.util.Log.e("PagoVM", "Error: ${exception.message}")
                _error.value = exception.message
                _estadoPago.value = EstadoTransbank.ERROR
            }
        }
    }

    fun confirmarPago(token: String) {
        viewModelScope.launch {
            repository.confirmarTransaccion(token)
                .onSuccess { response ->
                    _resultado.value = response
                    _estadoPago.value = if (response.response_code == 0) {
                        EstadoTransbank.AUTORIZADO
                    } else {
                        EstadoTransbank.RECHAZADO
                    }
                }.onFailure { exception ->
                    _error.value = exception.message
                    _estadoPago.value = EstadoTransbank.ERROR
                }
        }
    }

    fun resetear() {
        _estadoPago.value = EstadoTransbank.INICIANDO
        _transaccionData.value = null
        _resultado.value = null
        _error.value = null
    }
}