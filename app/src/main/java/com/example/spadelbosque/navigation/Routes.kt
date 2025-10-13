package com.example.spadelbosque.navigation

sealed class Route(val path: String){
    data object Home : Route("home")
}