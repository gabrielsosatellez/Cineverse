package com.project.cineversemobile.ViewModels

import ua.naiksoftware.stomp.StompClient
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.project.cineversemobile.API.RetrofitInstance
import com.project.cineversemobile.Data.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.LifecycleEvent


/**
 * ViewModel encargado de la gestión del chat en tiempo real.
 * Se conecta al backend mediante WebSockets (STOMP) y expone los mensajes recibidos.
 */
class ChatViewModel(application: Application) : AndroidViewModel(application) {

    // Lista de mensajes recibidos en el chat
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    // Cliente STOMP para la comunicación por WebSocket
    private lateinit var stompClient: StompClient

    // Conversor JSON para serializar y deserializar mensajes
    private val gson = Gson()

    /**
     * Establece la conexión con el servidor WebSocket y se suscribe al canal de mensajes.
     */
    fun connect() {
        val url = "ws://10.0.2.2:8080/ws-chat-native"
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)

        stompClient.lifecycle().subscribe({ event ->
            when (event.type) {
                LifecycleEvent.Type.OPENED -> {
                    Log.d("WS", "WebSocket conectado")

                    stompClient.topic("/topic/messages").subscribe(
                        { stompMessage ->
                            Log.d("WS", "Mensaje recibido: ${stompMessage.payload}")
                            val msg = gson.fromJson(stompMessage.payload, ChatMessage::class.java)
                            _messages.value = _messages.value + msg
                        },
                        { error ->
                            Log.e("WS", "Error recibiendo mensajes", error)
                        }
                    )
                }

                LifecycleEvent.Type.ERROR -> {
                    Log.e("WS", "Error en WebSocket", event.exception)
                }

                LifecycleEvent.Type.CLOSED -> {
                    Log.d("WS", "WebSocket cerrado")
                }

                else -> Unit
            }
        }, { error ->
            Log.e("WS", "Error en lifecycle", error)
        })

        stompClient.connect()
    }

    /**
     * Envía un mensaje al servidor a través del canal STOMP.
     */
    fun sendMessage(message: ChatMessage) {
        if (!::stompClient.isInitialized || !stompClient.isConnected) {
            Log.e("WS", "Intento de enviar sin conexión WebSocket")
            return
        }

        stompClient.send("/app/chat.send", gson.toJson(message)).subscribe(
            {
                Log.d("WS", "Mensaje enviado")
            },
            { error ->
                Log.e("WS", "Error enviando mensaje", error)
            }
        )
    }

    /**
     * Cierra la conexión WebSocket cuando el ViewModel es destruido.
     */
    override fun onCleared() {
        super.onCleared()
        if (::stompClient.isInitialized) {
            stompClient.disconnect()
        }
    }
}