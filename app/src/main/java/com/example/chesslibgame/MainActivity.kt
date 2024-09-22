package com.example.chesslibgame

import Login
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chesslibgame.ui.theme.ChessLibGameTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.chesslibgame.ui.screens.RegisterScreen
import com.example.chesslibgame.ui.screens.StartSc
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChessLibGameTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigator(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppNavigator(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash", modifier = modifier) {
        // Pantalla splash
        composable("splash") {
            Splash(onSplashCompleted = {
                navController.navigate("startSc")
            })
        }
        // Pantalla StartSc
        composable("startSc") {
            StartSc(onPlayClicked = {
                navController.navigate("login")
            })
        }
        // Pantalla de login
        composable("login") {
            Login(auth = FirebaseAuth.getInstance(), onLoginSuccess = {
                // Lógica del login exitoso
            }, navController = navController)
        }

        // Pantalla de registro
        composable("register") {
            RegisterScreen(
                auth = FirebaseAuth.getInstance(),
                onRegisterSuccess = {
                    // Después de un registro exitoso, navega al login
                    navController.navigate("login")
                },
                onBackToLogin = {
                    // Navega de vuelta a la pantalla de login
                    navController.navigate("login")
                }
            )
        }
    }
}

@Composable
fun Splash(onSplashCompleted: () -> Unit = {}) {
    val scope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        scope.launch {
            delay(2000)  // Duración de la pantalla splash
            onSplashCompleted()
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),  // Fondo negro para el splash
        contentAlignment = Alignment.Center
    ) {
        //logo
        Image(
            painter = painterResource(id = R.drawable.chess_logo),
            contentDescription = "Game Logo",
            modifier = Modifier.size(250.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen() {
    Splash(onSplashCompleted = {})
}