package com.example.spadelbosque.di

import com.example.spadelbosque.repository.CartRepository
import com.example.spadelbosque.repository.CartRepositoryImpl

// "Singletons" de la app (mock DI)
object AppGraph {
    // Un solo carrito para toda la app
    val cartRepo: CartRepository = CartRepositoryImpl()
}
