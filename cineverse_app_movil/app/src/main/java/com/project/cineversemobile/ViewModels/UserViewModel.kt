package com.project.cineversemobile.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.cineversemobile.API.RetrofitInstance
import com.project.cineversemobile.Data.LoginRequest
import com.project.cineversemobile.Data.RegisterRequest
import com.project.cineversemobile.Data.User
import com.project.cineversemobile.Data.UserPreferences
import kotlinx.coroutines.launch
import androidx.lifecycle.*

/**
 * ViewModel encargado de la gestión de la autenticación del usuario.
 * Controla el login, registro, cierre de sesión y eliminación de cuenta.
 */
class UserViewModel(application: Application) : AndroidViewModel(application) {

    // Gestor de preferencias para persistencia del usuario autenticado
    private val prefs = UserPreferences(application)

    // Usuario actualmente autenticado (persistido en DataStore)
    val loggedUser: LiveData<User?> =
        prefs.loggedUserFlow.asLiveData()

    // Mensaje de error en el proceso de login
    private val _loginError = MutableLiveData<String?>()
    val loginError: LiveData<String?> = _loginError

    // Mensaje de error en el proceso de registro
    private val _registerError = MutableLiveData<String?>()
    val registerError: LiveData<String?> = _registerError

    // Estado de carga durante el registro de usuario
    private val _isRegisterLoading = MutableLiveData(false)
    val isRegisterLoading: LiveData<Boolean> = _isRegisterLoading

    /**
     * Realiza el login del usuario contra el backend.
     * Si es correcto, guarda el usuario en almacenamiento local.
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val user = RetrofitInstance.authApi.login(
                    LoginRequest(
                        email = email.trim().lowercase(),
                        password = password.trim()
                    )
                )

                prefs.saveUser(user)
                _loginError.value = null

            } catch (e: Exception) {
                _loginError.value = "Email o contraseña incorrectos"
            }
        }
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * Realiza validaciones básicas antes de enviar la petición al backend.
     */
    fun register(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String,
        onSuccess: () -> Unit
    ) {
        if (fullName.isBlank()) {
            _registerError.value = "El nombre es obligatorio"
            return
        }

        if (email.isBlank()) {
            _registerError.value = "El email es obligatorio"
            return
        }

        if (password.isBlank()) {
            _registerError.value = "La contraseña es obligatoria"
            return
        }

        if (password != confirmPassword) {
            _registerError.value = "Las contraseñas no coinciden"
            return
        }

        viewModelScope.launch {
            try {
                _isRegisterLoading.value = true

                RetrofitInstance.authApi.register(
                    RegisterRequest(
                        fullName = fullName.trim(),
                        email = email.trim().lowercase(),
                        password = password.trim()
                    )
                )

                _registerError.value = null
                onSuccess()

            } catch (e: Exception) {
                _registerError.value = "Error al registrar usuario"
            } finally {
                _isRegisterLoading.value = false
            }
        }
    }

    /**
     * Cierra la sesión del usuario actual.
     * Elimina los datos persistidos en almacenamiento local.
     */
    fun logout() {
        viewModelScope.launch {
            prefs.clearUser()
        }
    }

    /**
     * Elimina la cuenta del usuario autenticado.
     * Llama al backend y limpia los datos locales.
     */
    fun deleteAccount(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val user = loggedUser.value ?: return@launch

                RetrofitInstance.authApi.deleteUser(user.id)

                prefs.clearUser()
                onSuccess()

            } catch (e: Exception) {
                e.stackTrace
            }
        }
    }
}

