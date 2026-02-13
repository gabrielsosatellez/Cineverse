package com.project.cineversemobile.Util

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.cineversemobile.Navigation.Routes
import com.project.cineversemobile.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarApp(
    navController: NavHostController,
    showBack: Boolean = true,
    showHome: Boolean = true
) {
    val darkColor = Color(0xFF0F172A)
    val lightText = Color.White

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = darkColor,
            titleContentColor = lightText,
            navigationIconContentColor = lightText,
            actionIconContentColor = lightText
        ),

        // Botón de navegación hacia atrás
        navigationIcon = {
            if (showBack) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
            }
        },

        // Título con logo de la aplicación
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.logo_cineverse),
                    contentDescription = "CineVerse",
                    modifier = Modifier.size(36.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "CineVerse",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },

        // Acciones de la barra superior
        actions = {
            if (showHome) {
                IconButton(
                    onClick = {
                        navController.navigate(Routes.Main.route) {
                            popUpTo(Routes.Main.route) { inclusive = false }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Inicio"
                    )
                }
            }
        }
    )
}