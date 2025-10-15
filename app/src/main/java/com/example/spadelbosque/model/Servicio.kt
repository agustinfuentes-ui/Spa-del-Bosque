package com.example.spadelbosque.model

data class Servicio(
    val sku: String,
    val categoria: String,
    val nombre: String,
    val precio: Int,
    val img: String,
    val duracionMin: Int
)