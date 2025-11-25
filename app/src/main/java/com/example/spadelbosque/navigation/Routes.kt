package com.example.spadelbosque.navigation

sealed class Route(val path: String){

    data object Splash : Route("splash")
    data object Login : Route("login")
    data object Registro: Route("registro")
    data object Home : Route("home")
    data object Servicios : Route("servicios")
    data object Blogs : Route("blogs")
    data object Nosotros : Route("nosotros")
    data object Contacto: Route("contacto")
    data object Carrito: Route("carrito")
    data object Perfil : Route("perfil")

    data object ServicioDetalle : Route("servicioDetalle/id"){
        fun whithId(id: String) = "servicioDetalle/$id"
    }
    data object BlogsDetalle : Route("blogsDetalle/id"){
        fun whithId(id: String) = "blogsDetalle/$id"
    }
    data object PagoTransbank : Route("pago_transbank?token={token}&url={url}") {
        fun withData(token: String, url: String): String {
            val encodedUrl = java.net.URLEncoder.encode(url, "UTF-8")
            return "pago_transbank?token=$token&url=$encodedUrl"
        }
    }
    data object Compra : Route(path= "compra")



}