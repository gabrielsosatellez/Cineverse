package com.cineverse.cineverse.chat;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    // Repositorio para acceder y persistir los mensajes del chat en la base de datos
    private final ChatMessageRepository chatMessageRepository;

    // Inyección de dependencias del repositorio de mensajes
    public ChatController(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    /**
     * Recibe un mensaje enviado por WebSocket desde los clientes.
     * Guarda el mensaje en base de datos y lo emite a todos los suscriptores.
     *
     * WebSocket endpoint: /app/chat.send
     * Topic de salida: /topic/messages
     */
    @MessageMapping("/chat.send")
    @SendTo("/topic/messages")
    public ChatMessage send(ChatMessage message) {
        chatMessageRepository.save(message);
        return message;
    }

    /**
     * Devuelve el historial completo del chat ordenado por ID (orden cronológico).
     * Endpoint REST: GET /api/chat/history
     */
    @GetMapping("/history")
    public List<ChatMessage> getChatHistory() {
        return chatMessageRepository.findAll(Sort.by("id"));
    }
}