package com.project.cineversemobile.Data

import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Long,
    val title: String,
    val description: String,
    val duration: Int,
    val imageUrl: String
)