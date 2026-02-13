import React, { useState } from "react";
import { postData, deleteData, fetchData } from "../../utils/api";
import { formatDateTime } from "../../utils/api";

export default function SessionsSection({
  sessions,
  setSessions,
  movies,
  rooms,
  isAdmin,
  selectedSession,
  setSelectedSession,
  setTakenSeats,
  setShowSeatModal
}) {
  const [movieId, setMovieId] = useState("");
  const [roomId, setRoomId] = useState("");
  const [dateTime, setDateTime] = useState("");
  const [price, setPrice] = useState(30);
  const [showConfirm, setShowConfirm] = useState(false);
  const [sessionToDelete, setSessionToDelete] = useState(null);
  const [error, setError] = useState("");

  // Recarga las sesiones desde el backend y sincroniza el estado local
  const refreshSessions = async () => {
    const data = await fetchData("http://localhost:8080/api/sessions");
    setSessions(data);

    // Limpia la sesión seleccionada si ha sido eliminada
    if (selectedSession && !data.find(s => s.id === selectedSession.id)) {
      setSelectedSession(null);
      setTakenSeats([]);
      setShowSeatModal(false);
    }
  };

  // Crea una nueva sesión asociando película, sala, fecha y precio
  const handleCreateSession = (e) => {
    e.preventDefault();
    setError("");
    const url = `http://localhost:8080/api/sessions?movieId=${movieId}&roomId=${roomId}&dateTime=${dateTime}&price=${price}`;
    postData(url, {})
      .then(response => {
        if (!response.ok) {
          throw new Error("Ya existe una sesión en esa sala a esa hora");
        }
        setMovieId("");
        setRoomId("");
        setDateTime("");
        setPrice(30);
        return refreshSessions();
      })
      .catch(err => {
        setError(err.message || "Error al crear la sesión");
        setDateTime("");
      });
  };

  // Elimina una sesión y actualiza el listado
  const handleDeleteSession = () => {
    deleteData(`http://localhost:8080/api/sessions/${sessionToDelete.id}`)
      .then(() => refreshSessions())
      .then(() => {
        setShowConfirm(false);
        setSessionToDelete(null);
      });
  };

  return (
    <>
      <h2 className="text-2xl font-bold mb-4">Sesiones</h2>

      {/* Formulario de creación de sesiones (solo visible para administradores) */}
      {isAdmin && (
        <form
          onSubmit={handleCreateSession}
          className="bg-white p-4 rounded shadow mb-6"
        >
          {error && (
            <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
              {error}
            </div>
          )}

          <select
            className="border p-2 w-full mb-2"
            value={movieId}
            onChange={e => setMovieId(e.target.value)}
            required
          >
            <option value="" disabled>Película</option>
            {movies.map(m => (
              <option key={m.id} value={m.id}>{m.title}</option>
            ))}
          </select>

          <select
            className="border p-2 w-full mb-2"
            value={roomId}
            onChange={e => setRoomId(e.target.value)}
            required
          >
            <option value="" disabled>Sala</option>
            {rooms.map(r => (
              <option key={r.id} value={r.id}>{r.name}</option>
            ))}
          </select>

          <input
            className="border p-2 w-full mb-2"
            type="datetime-local"
            value={dateTime}
            onChange={e => setDateTime(e.target.value)}
            required
          />

          <input
            className="border p-2 w-full mb-2"
            type="number"
            step="0.01"
            min="0"
            value={price}
            onChange={e => setPrice(parseFloat(e.target.value))}
            placeholder="Precio (€)"
            required
          />

          <button className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded transition-colors">
            Crear sesión
          </button>
        </form>
      )}

      <ul>
        {sessions.map(s => (
          <li
            key={s.id}
            className="bg-white p-3 mb-2 rounded shadow flex justify-between items-center"
          >
            <span>
              {s.movie.title} — {s.room.name} — {formatDateTime(s.dateTime)} - {s.price}€
            </span>

            {/* Acción de eliminación de sesión (solo para administradores) */}
            {isAdmin && (
              <button
                className="bg-red-500 hover:bg-red-600 text-white px-3 py-1 rounded transition-colors"
                onClick={() => {
                  setSessionToDelete(s);
                  setShowConfirm(true);
                }}
              >
                Eliminar
              </button>
            )}
          </li>
        ))}
      </ul>

      {/* Modal de confirmación para la eliminación de sesiones */}
      {showConfirm && sessionToDelete && (
        <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-lg p-6 w-96 text-center">
            <h3 className="text-xl font-bold mb-4">Eliminar sesión</h3>
            <p className="mb-6">
              ¿Seguro que quieres eliminar la sesión de<br />
              <span className="font-semibold">{sessionToDelete.movie.title}</span><br />
              en la sala <span className="font-semibold">{sessionToDelete.room.name}</span>?
            </p>
            <div className="flex gap-4">
              <button
                className="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded w-full transition-colors"
                onClick={handleDeleteSession}
              >
                Eliminar
              </button>
              <button
                className="bg-gray-400 hover:bg-gray-500 text-white px-4 py-2 rounded w-full transition-colors"
                onClick={() => {
                  setShowConfirm(false);
                  setSessionToDelete(null);
                }}
              >
                Cancelar
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
