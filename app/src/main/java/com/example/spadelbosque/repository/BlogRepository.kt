package com.example.spadelbosque.repository

import com.example.spadelbosque.R
import com.example.spadelbosque.model.BlogArticulo

// Repositorio que proporciona los datos de los artículos
object BlogRepository {

    fun getArticles(): List<BlogArticulo> {
        return listOf(
            BlogArticulo(
                id = 1,
                title = "Respiración consciente: reduce el estrés en 5 minutos",
                author = "Equipo SPA",
                date = "05/09/2025",
                excerpt = "Aprende una técnica breve para calmar el sistema nervioso antes de dormir o después del trabajo...",
                imageRes = R.drawable.ayurvedico // EL logo de momento
            ),
            BlogArticulo(
                id = 2,
                title = "Beneficios del masaje con piedras calientes",
                author = "Viviana",
                date = "01/09/2025",
                excerpt = "La termoterapia mejora la circulación y libera tensiones profundas. Te contamos cómo se realiza…",
                imageRes = R.drawable.piedras_calientes
            ),
            BlogArticulo(
                id = 3,
                title = "Circuito de aguas: guía para tu primera vez",
                author = "Rosa",
                date = "28/08/2025",
                excerpt = "Qué llevar, cómo prepararte y qué esperar de las distintas temperaturas del circuito…",
                imageRes = R.drawable.circuito
            )
        )
    }
}