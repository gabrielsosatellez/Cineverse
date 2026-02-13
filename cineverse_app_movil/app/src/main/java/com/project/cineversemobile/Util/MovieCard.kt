package com.project.cineversemobile.Util

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.project.cineversemobile.Data.Movie
import com.project.cineversemobile.R

@Composable
fun MovieCard(movie: Movie) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column {
            // Selección de la imagen de la película, utilizando un recurso local si no hay URL válida
            val imageModel = movie.imageUrl.takeIf { it.isNotBlank() }
                ?: R.drawable.no_poster

            AsyncImage(
                model = imageModel,
                contentDescription = movie.title,
                placeholder = painterResource(R.drawable.no_poster),
                error = painterResource(R.drawable.no_poster),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            // Registro de la URL de la imagen para depuración
            Log.d("MOVIE_IMAGE", "URL = '${movie.imageUrl}'")

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "${movie.duration} min",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = movie.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

