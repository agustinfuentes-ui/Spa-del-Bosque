package com.example.spadelbosque.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

// Define el DataStore como una extensión de Context para que esté disponible en toda la app
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sesion_usuario")
