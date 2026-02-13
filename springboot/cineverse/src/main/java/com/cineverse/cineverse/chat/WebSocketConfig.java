package com.cineverse.cineverse.chat;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Registra los endpoints WebSocket para la conexión de los clientes.
     * Se define un endpoint con soporte SockJS (para compatibilidad con navegadores antiguos)
     * y otro endpoint nativo sin SockJS (para la compatibilidad con AndroidStudio).
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*")
                .withSockJS();

        registry.addEndpoint("/ws-chat-native")
                .setAllowedOriginPatterns("*");
    }

    /**
     * Configura el broker de mensajería.
     * - /topic: prefijo para la publicación de mensajes a los clientes 
     * - /app: prefijo para los mensajes enviados desde el cliente al servidor
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }
}