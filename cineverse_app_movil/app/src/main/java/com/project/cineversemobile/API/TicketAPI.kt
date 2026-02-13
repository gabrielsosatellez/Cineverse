package com.project.cineversemobile.API

import com.project.cineversemobile.Data.Ticket
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TicketApi {

    // Obtiene los asientos ocupados de una sesión concreta
    @GET("/api/tickets/session/{sessionId}/occupied-seats")
    suspend fun getOccupiedSeats(
        @Path("sessionId") sessionId: Long
    ): List<Int>

    // Realiza la compra de entradas para una sesión y una lista de asientos
    @POST("api/tickets/buy")
    suspend fun buyTickets(
        @Query("sessionId") sessionId: Long,
        @Query("seatNumbers") seatNumbers: List<Int>,
        @Query("email") email: String
    ): List<Ticket>

    // Obtiene las entradas asociadas a un usuario concreto
    @GET("api/tickets/user/{userId}")
    suspend fun getUserTickets(
        @Path("userId") userId: Long
    ): List<Ticket>
}