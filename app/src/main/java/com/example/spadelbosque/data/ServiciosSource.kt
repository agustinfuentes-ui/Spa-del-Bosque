package com.example.spadelbosque.data

import com.example.spadelbosque.model.Servicio

object ServiciosSource {
    val servicios = listOf(
        // MASAJES
        Servicio(sku = "RELAX30", categoria = "masajes", nombre = "Masaje de Relajación 30 min", precio = 35000, img = "relajacion", duracionMin = 30),
        Servicio(sku = "RELAX40", categoria = "masajes", nombre = "Masaje de Relajación 40 min", precio = 40000, img = "relajacion", duracionMin = 40),
        Servicio(sku = "RELAX60", categoria = "masajes", nombre = "Masaje de Relajación 60 min", precio = 45000, img = "relajacion", duracionMin = 60),
        Servicio(sku = "DESC60",  categoria = "masajes", nombre = "Masaje Descontracturante 60 min", precio = 55000, img = "descontracturante", duracionMin = 60),
        Servicio(sku = "PIED60",  categoria = "masajes", nombre = "Masaje Piedras Calientes 60 min", precio = 57000, img = "piedras_calientes", duracionMin = 60),
        Servicio(sku = "BOSQUE60",categoria = "masajes", nombre = "Masaje del Bosque 60 min", precio = 47000, img = "bosque", duracionMin = 60),

        // CORPORALES
        Servicio(sku = "OLIVO90", categoria = "corporales", nombre = "Olivoterapia 90 min", precio = 72000, img = "olivoterapia", duracionMin = 90),
        Servicio(sku = "CHOCO90", categoria = "corporales", nombre = "Chocolaterapia 90 min", precio = 87000, img = "chocolaterapia", duracionMin = 90),
        Servicio(sku = "EXFO30",  categoria = "corporales", nombre = "Exfoliación corporal 30 min", precio = 42000, img = "exfoliacion", duracionMin = 30),

        // CIRCUITOS & SAUNA
        Servicio(sku = "CIRC45",  categoria = "circuitos",  nombre = "Circuito de agua 45 min", precio = 20000, img = "circuito" , duracionMin = 45),
        Servicio(sku = "SAUNA30", categoria = "circuitos",  nombre = "Sauna seco 30 min", precio = 18000, img = "sauna" , duracionMin = 30),

        // PROGRAMAS INDIVIDUALES
        Servicio(sku = "RENO135", categoria = "individuales", nombre = "Renovación 135 min", precio = 87000, img = "renovacion", duracionMin = 135),
        Servicio(sku = "DULCE135",categoria = "individuales", nombre = "Dulce aventura en el Bosque 135 min", precio = 102000, img = "dulce_aventura", duracionMin = 135),
        Servicio(sku = "DAY135",  categoria = "individuales", nombre = "Day Use 135 min", precio = 107000, img = "day_use", duracionMin = 135),

        // PROGRAMAS PAREJAS
        Servicio(sku = "NAT115",  categoria = "parejas", nombre = "Programa Bosque Nativo 115 min", precio = 134000, img = "bosque_nativo", duracionMin = 115),
        Servicio(sku = "MAG135",  categoria = "parejas", nombre = "Programa Bosque Mágico 135 min", precio = 186000, img = "bosque_magico", duracionMin = 135),

        // ESCAPADA AMIGAS
        Servicio(sku = "AMIG115", categoria = "escapada-amigas", nombre = "Programa Escapada de amigas 115 min", precio = 276000, img = "escapada_amigas", duracionMin = 115)
    )
}