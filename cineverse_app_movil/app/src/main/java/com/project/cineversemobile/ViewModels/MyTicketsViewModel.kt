package com.project.cineversemobile.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.project.cineversemobile.API.RetrofitInstance
import com.project.cineversemobile.Data.Ticket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de la carga y gestión de los tickets comprados por el usuario.
 * Se comunica con el backend para obtener el historial de entradas del usuario autenticado.
 */
class MyTicketsViewModel(application: Application) : AndroidViewModel(application) {

    // Listado de tickets del usuario
    private val _tickets = MutableStateFlow<List<Ticket>>(emptyList())
    val tickets: StateFlow<List<Ticket>> = _tickets

    // Estado de carga durante la petición al servidor
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    /**
     * Obtiene los tickets asociados a un usuario concreto desde el backend.
     */
    fun loadUserTickets(userId: Long) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _tickets.value = RetrofitInstance.ticketApi.getUserTickets(userId)
            } catch (e: Exception) {
                _tickets.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}