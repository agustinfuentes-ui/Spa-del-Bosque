package com.example.spadelbosque.model

data class BlogArticulo(
    val id: Int,
    val title: String,
    val author: String,
    val date: String,
    val excerpt: String, // Descripción corta del artículo
    val imageRes: Int // ID del recurso de imagen
)