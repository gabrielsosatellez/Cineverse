package com.project.cineversemobile.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.cineversemobile.Navigation.Routes
import com.project.cineversemobile.Util.CineVerseLogoHeader
import com.project.cineversemobile.ViewModels.UserViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    // Estado local de los campos del formulario de login
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Estado de error de autenticación y usuario autenticado
    val loginError by userViewModel.loginError.observeAsState()
    val loggedUser by userViewModel.loggedUser.observeAsState()

    // Redirige a la pantalla principal cuando el usuario se autentica correctamente
    LaunchedEffect(loggedUser) {
        if (loggedUser != null) {
            navController.navigate(Routes.Main.route) {
                popUpTo(Routes.Login.route) { inclusive = true }
            }
        }
    }

    // Paleta de colores utilizada en la pantalla de login
    val darkCard = Color(0xFF0F172A)
    val lightText = Color.White
    val mutedText = Color(0xFFCBD5E1)
    val primaryButton = Color(0xFF4F6DB8)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .clip(RoundedCornerShape(16.dp))
                .background(darkCard)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Cabecera visual con el logotipo de la aplicación
            CineVerseLogoHeader()

            Text(
                text = "Iniciar sesión",
                style = MaterialTheme.typography.headlineSmall,
                color = lightText
            )

            Spacer(Modifier.height(24.dp))

            // Campo de entrada del email del usuario
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = mutedText) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = lightText,
                    unfocusedBorderColor = mutedText,
                    focusedTextColor = lightText,
                    unfocusedTextColor = lightText,
                    cursorColor = lightText
                )
            )

            Spacer(Modifier.height(12.dp))

            // Campo de entrada de la contraseña con opción de mostrar/ocultar
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña", color = mutedText) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible)
                                Icons.Default.VisibilityOff
                            else
                                Icons.Default.Visibility,
                            contentDescription = null,
                            tint = lightText
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = lightText,
                    unfocusedBorderColor = mutedText,
                    focusedTextColor = lightText,
                    unfocusedTextColor = lightText,
                    cursorColor = lightText
                )
            )

            Spacer(Modifier.height(24.dp))

            // Acción de inicio de sesión
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryButton,
                    contentColor = Color.White
                ),
                onClick = {
                    userViewModel.login(email, password)
                }
            ) {
                Text("Entrar")
            }

            // Visualización de errores de autenticación
            if (loginError != null) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = loginError!!,
                    color = Color(0xFFF87171),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(16.dp))

            // Enlace de navegación a la pantalla de registro
            TextButton(onClick = {
                navController.navigate(Routes.Register.route)
            }) {
                Text(
                    text = "¿No tienes cuenta? Regístrate",
                    color = mutedText
                )
            }
        }
    }
}
