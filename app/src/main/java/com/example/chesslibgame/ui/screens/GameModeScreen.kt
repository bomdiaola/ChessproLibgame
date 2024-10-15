package com.example.chesslibgame.ui.screens

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.runtime.getValue

@Composable
fun GameModeScreen(navController: NavController) {
    // Animación de colores para el fondo
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val animatedColor1 by infiniteTransition.animateColor(
        initialValue = Color(0xFF0D0D1E),
        targetValue = Color(0xFF1A1A35),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    val animatedColor2 by infiniteTransition.animateColor(
        initialValue = Color(0xFF1A1A35),
        targetValue = Color(0xFF0D0D1E),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    // Estructura de la UI con el mismo fondo animado
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(animatedColor1, animatedColor2)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Select Game Mode",
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón para "Play Online"
            Button(
                onClick = {
                    navController.navigate("play_online")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C8979))
            ) {
                Text(text = "Play Online", color = Color.White)
            }

            // Botón para "Play Bots"
            Button(
                onClick = {
                    navController.navigate("play_bots")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C8979))
            ) {
                Text(text = "Play Bots", color = Color.White)
            }

            // Botón para "Play a Friend"
            Button(
                onClick = {
                    navController.navigate("chessBoard")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C8979))
            ) {
                Text(text = "Play a Friend", color = Color.White)
            }
        }
    }
}
