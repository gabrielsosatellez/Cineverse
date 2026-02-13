import React, { useState, useEffect } from "react";
import { formatDateTime, fetchData } from "../../utils/api";
import SeatGrid from "./SeatGrid";
import SeatModal from "./SeatModal";

export default function TicketsSection({
  sessions,
  selectedSession,
  setSelectedSession,
  takenSeats,
  setTakenSeats
}) {
  const [showSeatModal, setShowSeatModal] = useState(false);
  const [selectedSeat, setSelectedSeat] = useState(null);
  const [seatInfo, setSeatInfo] = useState("");
  const [newSeat, setNewSeat] = useState("");

  // Actualiza periódicamente los asientos ocupados de la sesión seleccionada
  useEffect(() => {
    if (!selectedSession) return;

    const interval = setInterval(() => {
      fetchData(`http://localhost:8080/api/tickets/session/${selectedSession.id}`)
        .then(data => setTakenSeats(data.map(t => t.seatNumber)))
        .catch(err => console.error(err));
    }, 3000);

    return () => clearInterval(interval);
  }, [selectedSession, setTakenSeats]);

  // Consulta el estado de un asiento concreto al hacer clic
  const handleSeatClick = async (seatNumber) => {
    try {
      const response = await fetch(
        `http://localhost:8080/api/tickets/session/${selectedSession.id}/seat/${seatNumber}`
      );
      const data = response.ok ? await response.json() : { occupied: false };

      if (data.occupied) {
        setSeatInfo(`Asiento ${seatNumber} ocupado por ${data.username}`);
      } else {
        setSeatInfo(`Asiento ${seatNumber} disponible`);
      }

      setSelectedSeat(seatNumber);
      setShowSeatModal(true);
    } catch (err) {
      console.error(err);
    }
  };

  // Fuerza la recarga del estado de los asientos desde el backend
  const refreshSeats = async () => {
    if (!selectedSession) return;
    const data = await fetchData(`http://localhost:8080/api/tickets/session/${selectedSession.id}`);
    setTakenSeats(data.map(t => t.seatNumber));
  };

  return (
    <>
      <h2 className="text-2xl font-bold mb-4">Gestionar entradas</h2>

      {/* Selector de sesión */}
      <select
        className="border p-2 mb-4"
        required
        value={selectedSession?.id || ""}
        onChange={e => {
          const session = sessions.find(s => s.id === Number(e.target.value));
          if (!session) return;

          setSelectedSession(session);
          fetchData(`http://localhost:8080/api/tickets/session/${session.id}`)
            .then(data => setTakenSeats(data.map(t => t.seatNumber)));
        }}
      >
        <option value="" disabled>Selecciona sesión</option>
        {[...sessions]
          .sort((a, b) => new Date(a.dateTime) - new Date(b.dateTime))
          .map(s => (
            <option key={s.id} value={s.id}>
              {s.movie.title} — {s.room.name} — {formatDateTime(s.dateTime)}
            </option>
          ))}
      </select>

      {selectedSession && (
        <div className="max-w-5xl mx-auto mt-4">
          {/* Información de la sala y ocupación */}
          <div className="text-center mb-3 text-lg font-semibold flex justify-center items-center gap-6">
            <span>
              {selectedSession.room.name} · {selectedSession.room.totalSeats} asientos
            </span>
            <span className="text-red-600">
              Ocupados: {takenSeats.length}
            </span>
            <span className="text-green-600">
              Libres: {selectedSession.room.totalSeats - takenSeats.length}
            </span>
          </div>

          {/* Indicador de pantalla */}
          <div className="bg-gray-300 text-center py-1 mb-5 rounded font-bold w-2/3 mx-auto">
            PANTALLA
          </div>

          {/* Representación gráfica de la sala */}
          <SeatGrid
            selectedSession={selectedSession}
            takenSeats={takenSeats}
            onSeatClick={handleSeatClick}
          />
        </div>
      )}

      {/* Modal de gestión de asientos */}
      <SeatModal
        isOpen={showSeatModal}
        seatInfo={seatInfo}
        selectedSeat={selectedSeat}
        selectedSession={selectedSession}
        takenSeats={takenSeats}
        setTakenSeats={setTakenSeats}
        newSeat={newSeat}
        setNewSeat={setNewSeat}
        onClose={() => {
          setShowSeatModal(false);
          setSelectedSeat(null);
          setSeatInfo("");
          setNewSeat("");
        }}
        onRefresh={refreshSeats}
      />
    </>
  );
}
