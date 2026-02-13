package com.project.cineversemobile.API

import com.project.cineversemobile.Data.Room
import retrofit2.http.GET
import retrofit2.http.Path

interface RoomApi {

    // Obtiene la sala asociada a una sesión concreta
    @GET("/api/sessions/{sessionId}/room")
    suspend fun getRoomBySession(
        @Path("sessionId") sessionId: Long
    ): Room
}