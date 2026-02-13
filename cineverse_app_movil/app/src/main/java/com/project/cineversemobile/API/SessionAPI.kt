package com.project.cineversemobile.API

import com.project.cineversemobile.Data.Session
import retrofit2.http.GET
import retrofit2.http.Path

interface SessionApi {

    // Obtiene el listado completo de sesiones disponibles
    @GET("api/sessions")
    suspend fun getAll(): List<Session>

    // Obtiene la información de una sesión concreta por su identificador
    @GET("api/sessions/{id}")
    suspend fun getById(
        @Path("id") id: Long
    ): Session
}