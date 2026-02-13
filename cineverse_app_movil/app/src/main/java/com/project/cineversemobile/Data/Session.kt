package com.project.cineversemobile.Data

data class Session(
    val id: Long,
    val dateTime: String,
    val price: Double,
    val movieTitle: String?,
    val roomName: String?,
)