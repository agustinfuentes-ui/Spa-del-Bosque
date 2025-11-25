package com.example.spadelbosque.ui.screens.pago

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagoWebViewScreen(
    token: String,
    url: String,
    onPagoCompletado: (String) -> Unit, // token de retorno
    onCancelar: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pago Transbank") }
            )
        }
    ) { padding ->
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true

                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            url: String?
                        ): Boolean {
                            // Capturar la URL de retorno
                            if (url?.startsWith("myapp://payment/return") == true) {
                                val returnToken = url.substringAfter("token_ws=")
                                onPagoCompletado(returnToken)
                                return true
                            }
                            return false
                        }
                    }

                    // Cargar formulario con token
                    val html = """
                        <html>
                        <body onload="document.forms[0].submit()">
                            <form method="POST" action="$url">
                                <input type="hidden" name="token_ws" value="$token"/>
                            </form>
                        </body>
                        </html>
                    """.trimIndent()

                    loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
                }
            }
        )
    }
}