package com.example.chesslibgame.ui.screens

import android.util.Log
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import com.example.chesslibgame.R
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun RegisterScreen(auth: FirebaseAuth, onRegisterSuccess: () -> Unit, onBackToLogin: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var registerError by remember { mutableStateOf<String?>(null) }
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

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

    // Fondo animado con gradiente
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

            // Logotipo del caballito
            Image(
                painter = painterResource(id = R.drawable.chess_signup_logo),  // Asegúrate de tener el logotipo en tu drawable
                contentDescription = "Chess Logo",
                modifier = Modifier.size(300.dp)
            )

            // Input para el email
            CustomTextFieldWithBottomLine(
                value = email,
                onValueChange = { email = it },
                label = "Email"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input para la contraseña
            CustomTextFieldWithBottomLine(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input para confirmar contraseña con borde inferior
            CustomTextFieldWithBottomLine(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirm Password",
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Control de errores
            registerError?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Botón de registro
            Button(
                onClick = {
                    if (password == confirmPassword) {
                        isLoading = true
                        registerError = null
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                isLoading = false
                                if (task.isSuccessful) {
                                    val userId = auth.currentUser?.uid
                                    val user = hashMapOf(
                                        "userId" to userId,
                                        "email" to email
                                    )
                                    // Guardar el usuario en Firestore
                                    userId?.let {
                                        db.collection("users").document(it).set(user)
                                            .addOnSuccessListener {
                                                Log.d("Register", "Usuario guardado en Firestore")
                                                onRegisterSuccess()
                                            }
                                            .addOnFailureListener { e ->
                                                registerError = "Error al guardar usuario: ${e.message}"
                                            }
                                    }
                                } else {
                                    registerError = task.exception?.message
                                }
                            }
                    } else {
                        registerError = "Passwords do not match"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C8979))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text(text = "REGISTER", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de volver atrás al login
            OutlinedButton(
                onClick = onBackToLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                border = BorderStroke(1.dp, Color.White)
            ) {
                Text("Atrás", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CustomTextFieldWithBottomLine(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column {
        Text(
            text = label,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            visualTransformation = visualTransformation,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.Gray,
                cursorColor = Color.White,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.White,  // Solo la línea inferior blanca
                unfocusedIndicatorColor = Color.Gray
            ),
            keyboardOptions = KeyboardOptions.Default
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    val fakeAuth = FirebaseAuth.getInstance()
    RegisterScreen(auth = fakeAuth, onRegisterSuccess = {}, onBackToLogin = {})
}
