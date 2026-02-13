import React, { useState } from "react";
import { postData, deleteData, fetchData } from "../../utils/api";
import logo from "../common/LOGO_CineVerse.png";

export default function LoginForm({ onLoginSuccess, isRegister, setIsRegister }) {
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");
  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("");
  const [passwordConfirm, setPasswordConfirm] = useState("");
  const [showPasswordConfirm, setShowPasswordConfirm] = useState(false);

  /**
   * Maneja el login del usuario.
   * Llama al backend con email y contraseña y, si es correcto,
   * notifica al componente padre con los datos del usuario.
   */
  const handleLogin = (e) => {
    e.preventDefault();

    postData("http://localhost:8080/api/users/login", { email, password })
      .then(res => {
        if (!res.ok) throw new Error();
        return res.json();
      })
      .then(user => {
        onLoginSuccess(user);   // Propaga el usuario autenticado al estado global
        setError("");
      })
      .catch(() => setError("Credenciales incorrectas"));
  };

  /**
   * Maneja el registro de un nuevo usuario.
   * Valida que las contraseñas coincidan antes de enviar los datos al backend.
   * Si el registro es correcto, vuelve automáticamente al modo login.
   */
  const handleRegister = (e) => {
    e.preventDefault();

    // Validación simple de contraseñas en frontend
    if (password !== passwordConfirm) {
      setError("Las contraseñas no coinciden");
      return;
    }

    postData("http://localhost:8080/api/users/register", {
      fullName,
      email,
      password
    })
      .then(res => {
        if (!res.ok) {
          throw new Error("Error al crear usuario");
        }
        return res.json();
      })
      .then(() => {
        // Volver al formulario de login tras registro correcto
        setIsRegister(false);
        setError("Usuario creado, ahora inicia sesión");

        // Limpiar campos
        setPassword("");
        setFullName("");
        setEmail("");
        setPasswordConfirm("");
      })
      .catch(() => setError("Error al crear la cuenta"));
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-b from-white to-gray-100 p-4">
      <form
        onSubmit={isRegister ? handleRegister : handleLogin}
        className="bg-gray-900 p-8 rounded-lg shadow-lg w-full max-w-md"
      >
        <div className="text-center mb-6">
          <img
            src={logo}
            alt="CineVerse"
            className="w-48 h-48 mx-auto mb-4"
          />
        </div>

        <h2 className="text-xl font-bold mb-4 text-center text-gray-300">
          {isRegister ? "Crear Cuenta" : "Iniciar Sesión"}
        </h2>

        {error && (
          <p className="text-red-500 mb-3 p-2 bg-red-100 rounded text-sm">
            {error}
          </p>
        )}

        {isRegister && (
          <input
            className="border border-gray-600 bg-gray-800 text-white p-3 w-full mb-3 rounded focus:outline-none focus:border-blue-500"
            placeholder="Nombre Completo"
            value={fullName}
            onChange={e => setFullName(e.target.value)}
            required
          />
        )}

        <input
          className="border border-gray-600 bg-gray-800 text-white p-3 w-full mb-3 rounded focus:outline-none focus:border-blue-500"
          type="email"
          placeholder={isRegister ? "Correo Electrónico" : "Correo"}
          value={email}
          onChange={e => setEmail(e.target.value)}
          required
        />

        {/* Input de contraseña con botón para mostrar/ocultar */}
        <div className="relative mb-4">
          <input
            className="border border-gray-600 bg-gray-800 text-white p-3 w-full rounded focus:outline-none focus:border-blue-500 pr-10"
            type={showPassword ? "text" : "password"}
            placeholder="Contraseña"
            value={password}
            onChange={e => setPassword(e.target.value)}
            required
          />
          <button
            type="button"
            onClick={() => setShowPassword(s => !s)}
            className="absolute inset-y-0 right-2 flex items-center text-gray-400 hover:text-gray-200 transition-colors"
            aria-label={showPassword ? "Ocultar contraseña" : "Mostrar contraseña"}
          >
            {/* Iconos */}
            {showPassword ? (
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13.875 18.825A10.05 10.05 0 0112 19c-5 0-9-4.03-9-7s4-7 9-7c1.043 0 2.042.17 2.958.487M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 3l18 18" />
              </svg>
            ) : (
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M2.454 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.545 7-.802 2.697-3.047 4.84-5.545 5.657M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
              </svg>
            )}
          </button>
        </div>

        {/* Confirmación de contraseña solo en modo registro */}
        {isRegister && (
          <div className="relative mb-4">
            <input
              className="border border-gray-600 bg-gray-800 text-white p-3 w-full rounded focus:outline-none focus:border-blue-500 pr-10"
              type={showPasswordConfirm ? "text" : "password"}
              placeholder="Confirmar Contraseña"
              value={passwordConfirm}
              onChange={e => setPasswordConfirm(e.target.value)}
              required
            />
            <button
              type="button"
              onClick={() => setShowPasswordConfirm(s => !s)}
              className="absolute inset-y-0 right-2 flex items-center text-gray-400 hover:text-gray-200 transition-colors"
              aria-label={showPasswordConfirm ? "Ocultar contraseña" : "Mostrar contraseña"}
            >
              {showPasswordConfirm ? (
                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13.875 18.825A10.05 10.05 0 0112 19c-5 0-9-4.03-9-7s4-7 9-7c1.043 0 2.042.17 2.958.487M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 3l18 18" />
                </svg>
              ) : (
                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M2.454 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.545 7-.802 2.697-3.047 4.84-5.545 5.657M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                </svg>
              )}
            </button>
          </div>
        )}

        <button className="bg-blue-600 hover:bg-blue-700 text-white w-full p-3 rounded font-semibold mb-3 transition-colors">
          {isRegister ? "Crear cuenta" : "Entrar"}
        </button>

        {/* Cambio entre modo login y registro */}
        <p
          className="text-sm text-center text-blue-400 hover:text-blue-300 cursor-pointer transition-colors"
          onClick={() => {
            setIsRegister(!isRegister);
            setError("");
            setPassword("");
            setEmail("");
            setFullName("");
            setPasswordConfirm("");
          }}
        >
          {isRegister
            ? "¿Ya tienes cuenta? Inicia sesión"
            : "¿No tienes cuenta? Crea una"}
        </p>
      </form>
    </div>
  );
}