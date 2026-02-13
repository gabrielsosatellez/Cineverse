import React from "react";

export default function Header({ currentUser, onMenuToggle }) {

  // Cabecera principal de la aplicación con información del usuario autenticado
  return (
    <header className="bg-gray-900 text-white p-4 flex justify-between items-center fixed top-0 left-0 w-full z-40">
      
      {/* Botón para abrir/cerrar el menú lateral */}
      <button
        onClick={onMenuToggle}
        className="hover:opacity-80 transition-opacity"
      >
        ☰
      </button>

      {/* Información básica del usuario autenticado */}
      <span>
        {currentUser.username} ({currentUser.role})
      </span>
    </header>
  );
}