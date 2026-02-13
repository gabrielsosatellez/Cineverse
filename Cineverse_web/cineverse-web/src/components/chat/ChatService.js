import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

let client = null;

/**
 * Establece la conexión con el WebSocket del backend y se suscribe al topic de mensajes.
 * Cada vez que llega un mensaje, se ejecuta el callback onMessage.
 */
export function connectChat(onMessage) {
  client = new Client({
    webSocketFactory: () => new SockJS("http://localhost:8080/ws-chat"),
    onConnect: () => {
      client.subscribe("/topic/messages", msg => {
        onMessage(JSON.parse(msg.body));
      });
    }
  });

  client.activate();
}

/**
 * Envía un mensaje al backend a través del endpoint STOMP.
 * @param {string} sender - Email del usuario que envía el mensaje
 * @param {string} content - Contenido del mensaje
 */
export function sendMessage(sender, content) {
  if (client) {
    client.publish({
      destination: "/app/chat.send",
      body: JSON.stringify({ sender, content })
    });
  }
}

