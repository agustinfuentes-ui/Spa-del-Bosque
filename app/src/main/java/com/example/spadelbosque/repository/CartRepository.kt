package com.example.spadelbosque.repository

import com.example.spadelbosque.model.ItemCarrito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update


interface CartRepository {
    val items: StateFlow<List<ItemCarrito>>
    fun add(item: ItemCarrito)
    fun inc(sku: String)
    fun dec(sku: String)
    fun remove(sku: String)
    fun clear()
}

class CartRepositoryImpl : CartRepository {
    private val _items = MutableStateFlow<List<ItemCarrito>>(emptyList())
    override val items: StateFlow<List<ItemCarrito>> = _items

    override fun add(item: ItemCarrito) = _items.update { list ->
        val i = list.indexOfFirst { it.sku == item.sku }
        if (i >= 0) list.toMutableList().also { it[i] = it[i].copy(qty = it[i].qty + 1) }
        else list + item
    }

    override fun inc(sku: String) = _items.update { list ->
        list.map { if (it.sku == sku) it.copy(qty = it.qty + 1) else it }
    }

    override fun dec(sku: String) = _items.update { list ->
        list.map { if (it.sku == sku) it.copy(qty = maxOf(1, it.qty - 1)) else it }
    }

    override fun remove(sku: String) = _items.update { list ->
        list.filterNot { it.sku == sku }
    }

    override fun clear() { _items.value = emptyList() }
}
