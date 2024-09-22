package com.example.chesslibgame.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.chesslibgame.R
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.auth.FirebaseAuth


@Composable
fun StartSc(onPlayClicked: () -> Unit) {
    // Pantalla después de la splash, con el logo y el botón "Play"
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0D0D1E), Color(0xFF1A1A35))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logotipo del juego
            Image(
                painter = painterResource(id = R.drawable.chess_logo),  // Reemplaza con el logo del juego
                contentDescription = "Game Logo",
                modifier = Modifier.size(250.dp)
            )

            Spacer(modifier = Modifier.height(50.dp))

            // Botón "Play"
            Button(
                onClick = onPlayClicked,
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C8979))
            ) {
                Text(text = "PLAY", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
    fun PreviewStartSc() { StartSc(onPlayClicked = {})
    }