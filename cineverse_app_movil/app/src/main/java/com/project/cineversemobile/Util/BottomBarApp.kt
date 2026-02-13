package com.project.cineversemobile.Util

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BottomBarApp(
    fullName: String,
    onMenuClick: () -> Unit
) {
    val darkColor = Color(0xFF0F172A)
    val lightText = Color.White

    BottomAppBar(
        containerColor = darkColor
    ) {
        // Muestra el nombre del usuario autenticado en la barra inferior
        Text(
            text = fullName,
            color = lightText,
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Botón para abrir el menú lateral
        IconButton(onClick = onMenuClick) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menú",
                tint = lightText
            )
        }
    }
}
