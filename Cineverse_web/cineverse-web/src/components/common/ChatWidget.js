import React, { useState } from "react";
import ChatBox from "../chat/ChatBox";

export default function ChatWidget({ user }) {
  const [showChat, setShowChat] = useState(false);

  // Controla la visibilidad del widget flotante del chat
  return (
    <>
      {/* Botón flotante para abrir el chat */}
      <button
        onClick={() => setShowChat(true)}
        className="fixed bottom-6 right-6 bg-blue-600 hover:bg-blue-700 text-white
                   w-14 h-14 rounded-full shadow-lg flex items-center justify-center
                   text-2xl z-50 transition-colors"
        title="Chat con clientes"
      >
        <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
        </svg>
      </button>

      {/* Ventana flotante del chat */}
      {showChat && (
        <div className="fixed bottom-24 right-6 w-80 h-96 bg-white rounded-lg shadow-xl
                        flex flex-col z-50">
          {/* Cabecera del chat */}
          <div className="bg-blue-600 text-white p-3 rounded-t-lg
                          flex justify-between items-center">
            <span className="font-semibold">Chat CineVerse</span>
            <button
              onClick={() => setShowChat(false)}
              className="text-white font-bold hover:opacity-80 transition-opacity"
            >
              ✕
            </button>
          </div>

          {/* Contenedor del componente principal del chat */}
          <div className="flex-1 overflow-hidden">
            <ChatBox user={user} />
          </div>
        </div>
      )}
    </>
  );
}