import React, { useState } from "react";
import { postData, deleteData, fetchData } from "../../utils/api";

export default function RoomsSection({
  rooms,
  setRooms,
  sessions,
  setSessions,
  isAdmin,
  selectedSession,
  setSelectedSession,
  setTakenSeats,
  setShowSeatModal
}) {
  const [newRoomName, setNewRoomName] = useState("");
  const [newRoomSeats, setNewRoomSeats] = useState("");
  const [showConfirm, setShowConfirm] = useState(false);
  const [roomToDelete, setRoomToDelete] = useState(null);

  // Recarga salas y sesiones desde el backend y sincroniza el estado local
  const refreshData = async () => {
    const roomsData = await fetchData("http://localhost:8080/api/rooms");
    const sessionsData = await fetchData("http://localhost:8080/api/sessions");
    setRooms(roomsData);
    setSessions(sessionsData);

    // Limpia la sesión seleccionada si ha sido eliminada
    if (selectedSession && !sessionsData.find(s => s.id === selectedSession.id)) {
      setSelectedSession(null);
      setTakenSeats([]);
      setShowSeatModal(false);
    }
  };

  // Crea una nueva sala en el backend y actualiza el listado
  const handleAddRoom = (e) => {
    e.preventDefault();
    postData("http://localhost:8080/api/rooms", {
      name: newRoomName,
      totalSeats: newRoomSeats
    }).then(() => {
      setNewRoomName("");
      setNewRoomSeats("");
      return refreshData();
    });
  };

  // Elimina una sala y actualiza salas y sesiones asociadas
  const handleDeleteRoom = () => {
    deleteData(`http://localhost:8080/api/rooms/${roomToDelete.id}`)
      .then(() => refreshData())
      .then(() => {
        setShowConfirm(false);
        setRoomToDelete(null);
      });
  };

  return (
    <>
      <h2 className="text-2xl font-bold mb-4">Salas</h2>

      {/* Formulario de creación de salas (solo visible para administradores) */}
      {isAdmin && (
        <form
          className="bg-white p-4 rounded shadow mb-6"
          onSubmit={handleAddRoom}
        >
          <h3 className="font-bold mb-2">Crear sala</h3>

          <input
            className="border p-2 w-full mb-2"
            placeholder="Nombre"
            value={newRoomName}
            onChange={e => setNewRoomName(e.target.value)}
          />

          <input
            className="border p-2 w-full mb-2"
            type="number"
            placeholder="Asientos"
            value={newRoomSeats}
            onChange={e => setNewRoomSeats(e.target.value)}
          />

          <button className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded transition-colors">
            Crear sala
          </button>
        </form>
      )}

      <ul>
        {rooms.map(room => (
          <li
            key={room.id}
            className="bg-white p-3 mb-2 rounded shadow flex justify-between items-center"
          >
            <span>{room.name} — {room.totalSeats} asientos</span>

            {/* Acción de eliminación de sala (solo para administradores) */}
            {isAdmin && (
              <button
                className="bg-red-500 hover:bg-red-600 text-white px-3 py-1 rounded transition-colors"
                onClick={() => {
                  setRoomToDelete(room);
                  setShowConfirm(true);
                }}
              >
                Eliminar
              </button>
            )}
          </li>
        ))}
      </ul>

      {/* Modal de confirmación para la eliminación de salas */}
      {showConfirm && roomToDelete && (
        <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-lg p-6 w-96 text-center">
            <h3 className="text-xl font-bold mb-4">Eliminar sala</h3>
            <p className="mb-6">
              ¿Seguro que quieres eliminar la sala<br />
              <span className="font-semibold">{roomToDelete.name}</span>?
              <br />
              <span className="text-sm text-red-600">
                (Se eliminarán también sus sesiones)
              </span>
            </p>
            <div className="flex gap-4">
              <button
                className="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded w-full transition-colors"
                onClick={handleDeleteRoom}
              >
                Eliminar
              </button>
              <button
                className="bg-gray-400 hover:bg-gray-500 text-white px-4 py-2 rounded w-full transition-colors"
                onClick={() => {
                  setShowConfirm(false);
                  setRoomToDelete(null);
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
