package com.project.cineversemobile.API

import com.project.cineversemobile.Data.Movie
import retrofit2.http.GET

interface MovieApi {

    // Obtiene el listado completo de películas desde el backend
    @GET("/api/movies")
    suspend fun getAllMovies(): List<Movie>
}
