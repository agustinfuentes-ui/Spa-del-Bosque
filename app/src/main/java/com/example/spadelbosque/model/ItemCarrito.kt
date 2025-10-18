package com.example.spadelbosque.model


data class ItemCarrito(
    val sku: String,
    val nombre: String,
    val precio: Int,
    val qty: Int = 1
)