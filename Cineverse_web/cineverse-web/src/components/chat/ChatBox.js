import React, { useEffect, useRef, useState } from "react";
import { connectChat, sendMessage } from "./ChatService";

export default function ChatBox({ user }) {
  const messagesEndRef = useRef(null);

  const [messages, setMessages] = useState([]);
  const [text, setText] = useState("");
  const [otherPersonName, setOtherPersonName] = useState("");
  const [loadedFromStorage, setLoadedFromStorage] = useState(false);
  const [showClearConfirm, setShowClearConfirm] = useState(false);

  // Clave dinámica para persistir el chat por usuario en localStorage
  const STORAGE_KEY = user?.email
    ? `cineverse_chat_messages_${user.email}`
    : null;

  // Indica si el usuario es admin/empleado (vista de soporte)
  const isAdmin = user?.role && user.role !== "USER";

  // CARGAR DESDE LOCALSTORAGE
  useEffect(() => {
    if (!STORAGE_KEY) return;

    const saved = localStorage.getItem(STORAGE_KEY);
    if (saved) {
      setMessages(JSON.parse(saved));
    }
    setLoadedFromStorage(true);
  }, [STORAGE_KEY]);

  // GUARDAR EN LOCALSTORAGE
  useEffect(() => {
    if (!STORAGE_KEY || !loadedFromStorage) return;
    localStorage.setItem(STORAGE_KEY, JSON.stringify(messages));
  }, [messages, STORAGE_KEY, loadedFromStorage]);

  // CONEXIÓN AL WEBSOCKET
  useEffect(() => {
    if (!loadedFromStorage) return;

    // Se conecta al chat en tiempo real y recibe mensajes entrantes
    connectChat(msg => {
      setMessages(prev => {
        const exists = prev.some(
          m => m.sender === msg.sender && m.content === msg.content
        );
        if (exists) return prev;
        return [...prev, msg];
      });

      // Guarda el nombre del otro usuario para mostrarlo en vista admin
      if (!otherPersonName && msg.sender !== user.email) {
        setOtherPersonName(msg.sender);
      }
    });
  }, [loadedFromStorage]);

  // AUTO SCROLL AL ÚLTIMO MENSAJE
  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  // Envía un mensaje por WebSocket
  const handleSend = () => {
    if (!text.trim()) return;
    sendMessage(user.email, text);
    setText("");
  };

  // Abre el modal de confirmación para vaciar el chat
  const handleClearChat = () => {
    setShowClearConfirm(true);
  };

  // Elimina los mensajes del estado y del localStorage
  const confirmClearChat = () => {
    setMessages([]);
    if (STORAGE_KEY) {
      localStorage.removeItem(STORAGE_KEY);
    }
    setShowClearConfirm(false);
  };

  // Permite enviar el mensaje con Enter (sin salto de línea)
  const handleKeyPress = e => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  };

  return (
    <div className="flex flex-col h-full p-3">
      <div className="font-bold mb-2">
        Chat de soporte
        {isAdmin && otherPersonName && ` - ${otherPersonName}`}
      </div>

      <div className="flex-1 overflow-y-auto mb-2 text-sm">
        {messages.map((m, i) => {
          const isMe = m.sender === user.email;

          return (
            <div
              key={i}
              className={`mb-2 flex ${isMe ? "justify-end" : "justify-start"}`}
            >
              <div
                className={`px-3 py-2 rounded-lg max-w-[70%]
                  ${isMe ? "bg-blue-600 text-white" : "bg-gray-300 text-gray-900"}`}
              >
                <div className="font-semibold text-xs mb-1">
                  {isMe ? "Tú" : m.sender}
                </div>
                {m.content}
              </div>
            </div>
          );
        })}
        <div ref={messagesEndRef} />
      </div>

      <input
        className="border p-2 w-full mb-1 rounded"
        value={text}
        onChange={e => setText(e.target.value)}
        onKeyPress={handleKeyPress}
        placeholder="Escribe un mensaje..."
      />

      <div className="flex gap-2">
        <button
          className="bg-blue-600 text-white flex-1 py-1 rounded hover:bg-blue-700"
          onClick={handleSend}
        >
          Enviar
        </button>

        <button
          className="bg-red-600 text-white py-1 px-3 rounded hover:bg-red-700 transition-colors flex items-center justify-center"
          onClick={handleClearChat}
          title="Vaciar chat"
        >
          {/* Icono de papelera */}
          <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
          </svg>
        </button>
      </div>

      {/* Modal de confirmación para vaciar el chat */}
      {showClearConfirm && (
        <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-lg p-6 w-96 text-center">
            <h3 className="text-xl font-bold mb-4">Vaciar chat</h3>
            <p className="mb-6">
              ¿Estás seguro de que deseas vaciar todos los mensajes?
            </p>
            <div className="flex gap-4">
              <button
                className="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded w-full transition-colors"
                onClick={confirmClearChat}
              >
                Vaciar
              </button>
              <button
                className="bg-gray-400 hover:bg-gray-500 text-white px-4 py-2 rounded w-full transition-colors"
                onClick={() => setShowClearConfirm(false)}
              >
                Cancelar
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
