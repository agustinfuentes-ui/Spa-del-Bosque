package com.example.spadelbosque.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.LinearEasing
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Modifier
import com.example.spadelbosque.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import kotlinx.coroutines.delay
import com.example.spadelbosque.viewmodel.AuthViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle


private const val BREATH_HALF_CYCLE_MS = 1200   //  1 fase (inhalar o exhalar)
private const val BREATH_CYCLES_TO_SHOW = 2     // cuántos ciclos completos mostrar
// Un ciclo completo = inhalar + exhalar = 2 * BREATH_HALF_CYCLE_MS
private val MIN_SPLASH_MS = (BREATH_CYCLES_TO_SHOW * 2 * BREATH_HALF_CYCLE_MS).toLong()

@Composable
fun SplashScreen(
    authVm: AuthViewModel,
    onFinished: (isLoggedIn: Boolean) -> Unit
) {
    // 1) Observamos la sesión (null = no hay sesión / Usuario = hay sesión)
    val sesion by authVm.sesionState.collectAsStateWithLifecycle()

    // 2) Temporizador mínimo del splash
    var minTimePassed by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(MIN_SPLASH_MS )
        minTimePassed = true
    }

    // 3) Disparo de navegación UNA sola vez cuando ambas condiciones se cumplen
    var navigated by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(sesion, minTimePassed) {
        if (!navigated && minTimePassed) {
            navigated = true
            onFinished(sesion != null)
        }
    }
    // 4) Animación “respiración”
    SplashBreathingUI()
}

@Composable
private fun SplashBreathingUI() {
    val infinite = rememberInfiniteTransition(label = "breath")
    val scale by infinite.animateFloat(
        initialValue = 0.92f, targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(BREATH_HALF_CYCLE_MS, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    val alpha by infinite.animateFloat(
        initialValue = 0.70f, targetValue = 1.00f,
        animationSpec = infiniteRepeatable(
            animation = tween(BREATH_HALF_CYCLE_MS, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE6F2EF)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Box(
                modifier = Modifier
                    .size(160.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                    }
                    .background(
                        color = Color.White,
                        shape = CircleShape
                    )
                    .border(
                        width = 4.dp,
                        color = Color(0xFF99D2CB).copy(alpha = 0.25f),
                        shape = CircleShape
                ),
                contentAlignment = Alignment.Center
            ){
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Logo SPA",
                    modifier = Modifier
                        .fillMaxSize(0.75f)  // 0.50–0.65 según te guste
                        .aspectRatio(1f),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(Modifier.height(20.dp))
            Text("Bienvenido", color = Color(0xFF0CA695))
            Spacer(Modifier.height(16.dp))
            CircularProgressIndicator(
                strokeWidth = 3.dp,
                trackColor = Color.LightGray.copy(alpha = 0.3f)
            )
            Spacer(Modifier.height(6.dp))
            Text("Respira…", color = Color(0xFF0CA695))
        }
    }
}