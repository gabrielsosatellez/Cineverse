import React, { useEffect } from "react";
import logo from "../common/LOGO_CineVerse.png";

export default function SplashScreen({ onComplete }) {

  // Ejecuta el callback tras 2 segundos para cerrar el splash screen
  useEffect(() => {
    const timer = setTimeout(() => {
      onComplete();
    }, 2000); // Mostrar splash durante 2 segundos

    return () => clearTimeout(timer);
  }, [onComplete]);

  return (
    <div className="fixed inset-0 bg-gradient-to-b from-gray-900 to-gray-800 flex flex-col items-center justify-center z-50">
      <div className="text-center">
        <img
          src={logo}
          alt="CineVerse"
          className="w-60 h-60 mx-auto mb-8"
        />
        
        {/* Indicador de carga mientras se muestra el splash */}
        <div className="flex justify-center mb-4">
          <div className="relative w-12 h-12">
            <div className="absolute inset-0 rounded-full border-4 border-gray-600"></div>
            <div className="absolute inset-0 rounded-full border-4 border-transparent border-t-blue-500 animate-spin"></div>
          </div>
        </div>
        
        <p className="text-gray-300 text-lg">Bienvenido</p>
      </div>
    </div>
  );
}
