package com.project.cineversemobile.API

import com.project.cineversemobile.Data.LoginRequest
import com.project.cineversemobile.Data.RegisterRequest
import com.project.cineversemobile.Data.User
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApi {

    // Realiza la autenticación del usuario contra el backend
    @POST("/api/users/login")
    suspend fun login(
        @Body request: LoginRequest
    ): User

    // Registra un nuevo usuario en el sistema
    @POST("/api/users/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): User

    // Elimina la cuenta de un usuario por su identificador
    @DELETE("/api/users/{id}")
    suspend fun deleteUser(
        @Path("id") userId: Long
    )
}