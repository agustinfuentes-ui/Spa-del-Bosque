package com.example.spadelbosque.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spadelbosque.repository.CartRepository
import com.example.spadelbosque.viewmodel.CarritoViewModel

class CarritoVmFactory(
    private val repo: CartRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarritoViewModel::class.java)) {
            return CarritoViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
