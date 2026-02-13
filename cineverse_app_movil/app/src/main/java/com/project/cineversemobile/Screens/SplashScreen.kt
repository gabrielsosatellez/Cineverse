package com.project.cineversemobile.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.cineversemobile.Navigation.Routes
import com.project.cineversemobile.R
import com.project.cineversemobile.ViewModels.UserViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    // Usuario autenticado almacenado en el estado del ViewModel
    val loggedUser by userViewModel.loggedUser.observeAsState()

    // Decide la pantalla de destino en función de si existe un usuario autenticado
    LaunchedEffect(loggedUser) {
        delay(1200)

        if (loggedUser != null) {
            navController.navigate(Routes.Main.route) {
                popUpTo(Routes.Splash.route) { inclusive = true }
            }
        } else {
            navController.navigate(Routes.Login.route) {
                popUpTo(Routes.Splash.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Logo de la aplicación mostrado durante la carga inicial
            Box(
                modifier = Modifier.size(140.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_cineverse),
                    contentDescription = "CineVerse logo",
                    modifier = Modifier.size(160.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Indicador de carga mientras se resuelve la navegación inicial
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 3.dp
            )
        }
    }
}
