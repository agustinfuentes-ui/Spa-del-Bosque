package com.example.spadelbosque.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontWeight

val Typography = Typography().run {
    copy(
        headlineMedium = headlineMedium.copy(fontWeight = FontWeight.SemiBold),
        titleLarge     = titleLarge.copy(fontWeight = FontWeight.SemiBold),
        labelLarge     = labelLarge.copy(fontWeight = FontWeight.SemiBold),
        bodyLarge      = bodyLarge.copy(color = TextoPrimario),
        bodyMedium     = bodyMedium.copy(color = TextoSecundario)
    )
}
