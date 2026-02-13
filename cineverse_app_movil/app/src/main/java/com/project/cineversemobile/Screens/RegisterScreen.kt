package com.project.cineversemobile.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.project.cineversemobile.Util.CineVerseLogoHeader
import com.project.cineversemobile.ViewModels.UserViewModel

@Composable
fun RegisterScreen(
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    // Estados locales de los campos del formulario
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Estados expuestos por el ViewModel para el proceso de registro
    val isLoading by userViewModel.isRegisterLoading.observeAsState(false)
    val registerError by userViewModel.registerError.observeAsState()

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

            CineVerseLogoHeader()

            Text(
                text = "Crear cuenta",
                style = MaterialTheme.typography.headlineSmall,
                color = lightText
            )

            Spacer(Modifier.height(24.dp))

            // Campo de nombre completo
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Nombre", color = mutedText) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isLoading,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = lightText,
                    unfocusedBorderColor = mutedText,
                    focusedTextColor = lightText,
                    unfocusedTextColor = lightText,
                    cursorColor = lightText
                )
            )

            Spacer(Modifier.height(12.dp))

            // Campo de email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = mutedText) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isLoading,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = lightText,
                    unfocusedBorderColor = mutedText,
                    focusedTextColor = lightText,
                    unfocusedTextColor = lightText,
                    cursorColor = lightText
                )
            )

            Spacer(Modifier.height(12.dp))

            // Campo de contraseña con control de visibilidad
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña", color = mutedText) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isLoading,
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

            Spacer(Modifier.height(12.dp))

            // Campo de confirmación de contraseña
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar contraseña", color = mutedText) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isLoading,
                visualTransformation = if (confirmPasswordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible)
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

            // Botón de envío del formulario de registro
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryButton,
                    contentColor = Color.White
                ),
                onClick = {
                    userViewModel.register(
                        fullName = fullName,
                        email = email,
                        password = password,
                        confirmPassword = confirmPassword
                    ) {
                        navController.popBackStack()
                    }
                }
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text("Registrarse")
                }
            }

            // Visualización de errores de registro
            if (registerError != null) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = registerError!!,
                    color = Color(0xFFF87171),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(16.dp))

            // Navegación de vuelta a la pantalla de login
            TextButton(
                onClick = { navController.popBackStack() },
                enabled = !isLoading
            ) {
                Text(
                    text = "Volver a iniciar sesión",
                    color = mutedText
                )
            }
        }
    }
}