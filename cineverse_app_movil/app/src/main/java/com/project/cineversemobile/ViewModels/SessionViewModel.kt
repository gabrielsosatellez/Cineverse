package com.project.cineversemobile.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.project.cineversemobile.API.RetrofitInstance
import com.project.cineversemobile.Data.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de la carga y gestión del listado de sesiones disponibles.
 * Se comunica con el backend para obtener las sesiones activas del cine.
 */
class SessionViewModel(application: Application) : AndroidViewModel(application) {

    // Listado de sesiones disponibles
    private val _sessions = MutableStateFlow<List<Session>>(emptyList())
    val sessions: StateFlow<List<Session>> = _sessions

    // Estado de carga durante la obtención de las sesiones
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    /**
     * Obtiene todas las sesiones disponibles desde el backend.
     */
    fun loadAllSessions() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _sessions.value = RetrofitInstance.sessionApi.getAll()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}