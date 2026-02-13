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
 * ViewModel encargado de la carga de datos necesarios para la selección de asientos.
 * Gestiona la información de la sala, los asientos ocupados y la sesión seleccionada.
 */
class SeatSelectionViewModel(application: Application) : AndroidViewModel(application) {

    // Número total de asientos de la sala asociada a la sesión
    private val _totalSeats = MutableStateFlow<Int?>(null)
    val totalSeats: StateFlow<Int?> = _totalSeats

    // Estado de carga durante la obtención de los datos
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Conjunto de asientos ya ocupados en la sesión
    private val _occupiedSeats = MutableStateFlow<Set<Int>>(emptySet())
    val occupiedSeats: StateFlow<Set<Int>> = _occupiedSeats

    // Información completa de la sesión seleccionada
    private val _session = MutableStateFlow<Session?>(null)
    val session: StateFlow<Session?> = _session

    /**
     * Carga la información de la sala, los asientos ocupados y la sesión asociada.
     * Realiza varias llamadas al backend para obtener todos los datos necesarios.
     */
    fun loadRoom(sessionId: Long) {
        viewModelScope.launch {
            _isLoading.value = true

            // Cargar número de asientos
            try {
                val room = RetrofitInstance.roomApi.getRoomBySession(sessionId)
                _totalSeats.value = room.totalSeats
            } catch (e: Exception) {
                _totalSeats.value = 0
            }

            // Cargar asientos ocupados
            try {
                val occupied = RetrofitInstance.ticketApi.getOccupiedSeats(sessionId)
                _occupiedSeats.value = occupied.toSet()
            } catch (e: Exception) {
                _occupiedSeats.value = emptySet()
            }

            // Cargar información de la sesión
            try {
                val session = RetrofitInstance.sessionApi.getById(sessionId)
                _session.value = session
            } catch (e: Exception) {
                _session.value = null
            }

            _isLoading.value = false
        }
    }
}