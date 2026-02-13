package com.project.cineversemobile.Data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        // Claves utilizadas para persistir los datos del usuario en DataStore
        private val USER_ID = longPreferencesKey("user_id")
        private val USER_FULL_NAME = stringPreferencesKey("user_full_name")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_ROLE = stringPreferencesKey("user_role")
    }

    // Flujo reactivo que expone el usuario autenticado almacenado en DataStore
    val loggedUserFlow = context.dataStore.data.map { prefs ->
        val id = prefs[USER_ID]
        val fullName = prefs[USER_FULL_NAME]
        val email = prefs[USER_EMAIL]
        val role = prefs[USER_ROLE]

        if (id != null && fullName != null && email != null && role != null) {
            User(
                id = id,
                fullName = fullName,
                email = email,
                role = role
            )
        } else {
            null
        }
    }

    // Guarda los datos del usuario autenticado en el almacenamiento local
    suspend fun saveUser(user: User) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID] = user.id
            prefs[USER_FULL_NAME] = user.fullName
            prefs[USER_EMAIL] = user.email
            prefs[USER_ROLE] = user.role
        }
    }

    // Elimina los datos del usuario almacenados localmente
    suspend fun clearUser() {
        context.dataStore.edit { it.clear() }
    }
}