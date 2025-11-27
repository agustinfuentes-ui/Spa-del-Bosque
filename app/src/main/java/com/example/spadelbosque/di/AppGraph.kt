package com.example.spadelbosque.di

import android.app.Application
import android.content.Context
import com.example.spadelbosque.data.local.AppDatabase
import com.example.spadelbosque.repository.AuthRepository
import com.example.spadelbosque.repository.AuthRepositoryImpl
import com.example.spadelbosque.repository.CartRepository
import com.example.spadelbosque.repository.CartRepositoryImpl

object AppGraph {
    lateinit var app: Application
        private set

    lateinit var db: AppDatabase
        private set

    lateinit var cartRepo: CartRepository
        private set

    // Declaración de AuthRepository
    lateinit var authRepo: AuthRepository
        private set

    fun inicializar(context: Context) {
        this.app = context as Application
        this.db = AppDatabase.getDatabase(context)
        // Corrección: Instanciar la implementación, no la interfaz
        this.cartRepo = CartRepositoryImpl()
        // Inicialización de la implementación de AuthRepository
        this.authRepo = AuthRepositoryImpl(context)
    }
}
