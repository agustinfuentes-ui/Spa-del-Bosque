package com.example.spadelbosque

import android.app.Application
import com.example.spadelbosque.di.AppGraph

class SpaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Inicializa el grafo de dependencias aquí
        AppGraph.inicializar(this)

    }
}
