package com.example.spadelbosque.ui.util

import java.text.NumberFormat
import java.util.Locale

val CLP: NumberFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply {
    maximumFractionDigits = 0
}

// Uso: CLP.format(servicio.precio)

