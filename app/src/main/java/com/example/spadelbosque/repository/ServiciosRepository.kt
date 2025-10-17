package com.example.spadelbosque.repository

import com.example.spadelbosque.data.ServiciosSource
import com.example.spadelbosque.model.Servicio

class ServiciosRepository {

    // Obtiene la lista completa de servicios desde la fuente de datos local
    fun getServicios(): List<Servicio> {
        return ServiciosSource.servicios
    }

    // Obtiene una lista de servicios filtrada por una categoría específica
    fun getServiciosPorCategoria(categoria: String): List<Servicio> {
        return ServiciosSource.servicios.filter { it.categoria == categoria }
    }

    // Obtiene todas las categorías únicas de los servicios
    fun getCategorias(): List<String> {
        return ServiciosSource.servicios.map { it.categoria }.distinct()
    }

    // Obtiene un servicio específico por su SKU (para la pantalla de detalle)
    fun getServicioPorSku(sku: String): Servicio? {
        return ServiciosSource.servicios.find { it.sku == sku }
    }
}
