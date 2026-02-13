package com.cineverse.cineverse.chat;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la entidad ChatMessage.
 * Permite realizar operaciones CRUD sobre los mensajes del chat.
 */
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}