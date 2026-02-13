package com.project.cineversemobile.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // URL base del backend accesible desde el emulador de Android
    private const val BASE_URL = "http://10.0.2.2:8080"

    // Instancia única de Retrofit para la aplicación
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Cliente de la API de autenticación
    val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    // Cliente de la API de películas
    val movieApi: MovieApi by lazy {
        retrofit.create(MovieApi::class.java)
    }

    // Cliente de la API de sesiones
    val sessionApi: SessionApi by lazy {
        retrofit.create(SessionApi::class.java)
    }

    // Cliente de la API de salas
    val roomApi: RoomApi by lazy {
        retrofit.create(RoomApi::class.java)
    }

    // Cliente de la API de tickets
    val ticketApi: TicketApi by lazy {
        retrofit.create(TicketApi::class.java)
    }
}
