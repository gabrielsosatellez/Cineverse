import { useState, useEffect } from "react";

export const useAuth = () => {
  const [currentUser, setCurrentUser] = useState(() => {
    try {
      const u = localStorage.getItem("currentUser");
      return u ? JSON.parse(u) : null;
    } catch (e) {
      return null;
    }
  });

  const [isRegister, setIsRegister] = useState(false);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  // Realiza el login contra el backend y persiste el usuario autenticado
  const login = (e) => {
    e.preventDefault();
    fetch("http://localhost:8080/api/users/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password })
    })
      .then(res => {
        if (!res.ok) throw new Error();
        return res.json();
      })
      .then(user => {
        setCurrentUser(user);
        try {
          localStorage.setItem("currentUser", JSON.stringify(user));
        } catch (e) {}
        setError("");
      })
      .catch(() => setError("Credenciales incorrectas"));
  };

  // Registra un nuevo usuario en el backend
  const register = (e) => {
    e.preventDefault();
    fetch("http://localhost:8080/api/users/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password })
    }).then(() => {
      setIsRegister(false);
      setError("Usuario creado, ahora inicia sesión");
    });
  };

  // Cierra la sesión del usuario y limpia el estado local
  const logout = () => {
    setCurrentUser(null);
    try {
      localStorage.removeItem("currentUser");
    } catch (e) {}
    setUsername("");
    setPassword("");
  };

  return {
    currentUser,
    setCurrentUser,
    isRegister,
    setIsRegister,
    username,
    setUsername,
    password,
    setPassword,
    error,
    setError,
    login,
    register,
    logout
  };
};
