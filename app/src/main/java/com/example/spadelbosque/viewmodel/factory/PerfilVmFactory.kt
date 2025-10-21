package com.example.spadelbosque.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spadelbosque.repository.AuthRepository
import com.example.spadelbosque.viewmodel.PerfilViewModel

class PerfilVmFactory(
    private val app: Application,
    private val auth: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(c: Class<T>): T {
        if (c.isAssignableFrom(PerfilViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PerfilViewModel(app, auth) as T
        }
        throw IllegalArgumentException("Unknown ViewModel ${c.name}")
    }
}
