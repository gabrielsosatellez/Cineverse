package com.project.cineversemobile.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.project.cineversemobile.API.RetrofitInstance
import com.project.cineversemobile.Data.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de la gestión del carrito de compra de entradas.
 * Mantiene los asientos seleccionados y gestiona el proceso de pago.
 */
class CartViewModel(application: Application) : AndroidViewModel(application) {

    // Lista de entradas añadidas al carrito
    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items

    // Estado de carga durante el proceso de pago
    private val _isPaying = MutableStateFlow(false)
    val isPaying: StateFlow<Boolean> = _isPaying

    // Mensaje de error en caso de fallo en el pago
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /**
     * Añade una lista de entradas al carrito.
     */
    fun addItems(newItems: List<CartItem>) {
        _items.value = _items.value + newItems
    }

    /**
     * Elimina una entrada concreta del carrito.
     */
    fun removeItem(item: CartItem) {
        _items.value = _items.value - item
    }

    /**
     * Vacía completamente el carrito.
     */
    fun clear() {
        _items.value = emptyList()
    }

    /**
     * Realiza la compra de las entradas del carrito.
     * Agrupa las entradas por sesión y realiza una llamada al backend por cada sesión.
     */
    fun pay(userEmail: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _isPaying.value = true
                _error.value = null

                val grouped = _items.value.groupBy { it.sessionId }

                for ((sessionId, items) in grouped) {
                    val seats = items.map { it.seatNumber }

                    RetrofitInstance.ticketApi.buyTickets(
                        sessionId = sessionId,
                        seatNumbers = seats,
                        email = userEmail
                    )
                }

                clear()
                onSuccess()

            } catch (e: Exception) {
                _error.value = e.message ?: "Error al pagar"
            } finally {
                _isPaying.value = false
            }
        }
    }
}

