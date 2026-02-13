package com.project.cineversemobile.Data

data class Ticket(
    val id: Long,
    val seatNumber: Int,
    val sessionId: Long,
    val userId: Long,
    val session: Session
)