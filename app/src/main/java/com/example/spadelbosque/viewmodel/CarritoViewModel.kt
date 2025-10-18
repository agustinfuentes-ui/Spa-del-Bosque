package com.example.spadelbosque.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spadelbosque.model.ItemCarrito
import com.example.spadelbosque.repository.CartRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.spadelbosque.ui.util.CLP


data class CarritoUiState(
    val items: List<ItemCarrito> = emptyList(),
    val total: Int = 0,
    val totalCLP: String = "$0",
    val count: Int = 0
)

class CarritoViewModel (
    private val repo: CartRepository
) : ViewModel() {


    val ui: StateFlow<CarritoUiState> = repo.items
        .map { list ->
            val tot = list.sumOf { it.precio * it.qty }
            CarritoUiState(
                items = list,
                total = tot,
                totalCLP = CLP.format(tot),
                count = list.sumOf { it.qty }
            )
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, CarritoUiState())

    fun add(item: ItemCarrito) = viewModelScope.launch { repo.add(item) }
    fun inc(sku: String) = viewModelScope.launch { repo.inc(sku) }
    fun dec(sku: String) = viewModelScope.launch { repo.dec(sku) }
    fun remove(sku: String) = viewModelScope.launch { repo.remove(sku) }
    fun clear() = viewModelScope.launch { repo.clear() }
}
