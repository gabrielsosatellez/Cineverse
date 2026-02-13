package com.project.cineversemobile.Data

data class CartItem(
    val sessionId: Long,
    val seatNumber: Int,
    val price: Double,
    val movieTitle: String,
    val dateTime: String,
    val roomName: String?
)