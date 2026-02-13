package com.project.cineversemobile.Util

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.project.cineversemobile.R

@Composable
fun CineVerseLogoHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Logo principal de la aplicación mostrado en pantallas de autenticación
        Image(
            painter = painterResource(id = R.drawable.logo_cineverse),
            contentDescription = "CineVerse logo",
            modifier = Modifier.size(160.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}