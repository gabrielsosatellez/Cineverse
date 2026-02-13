import React from "react";
import { fetchData, deleteData, putData } from "../../utils/api";

export default function SeatModal({
  isOpen,
  seatInfo,
  selectedSeat,
  selectedSession,
  takenSeats,
  setTakenSeats,
  newSeat,
  setNewSeat,
  onClose,
  onRefresh
}) {
  const [confirmAction, setConfirmAction] = React.useState(null);

  // Valida la acción solicitada (cambio o liberación de asiento) antes de confirmar
  const handleChangeOrDeleteSeat = async (action) => {
    if (action === "CHANGE") {
      const seatNum = Number(newSeat);

      if (!seatNum) {
        alert("Introduce un número válido");
        return;
      }

      if (seatNum < 1 || seatNum > selectedSession.room.totalSeats) {
        alert("Asiento fuera del rango de la sala");
        return;
      }

      if (takenSeats.includes(seatNum)) {
        alert("Ese asiento ya está ocupado");
        return;
      }

      setConfirmAction("CHANGE");
    } else if (action === "DELETE") {
      setConfirmAction("DELETE");
    }
  };

  // Ejecuta la acción confirmada contra el backend y refresca el estado
  const handleConfirm = async () => {
    if (confirmAction === "CHANGE") {
      await putData(
        `http://localhost:8080/api/tickets/session/${selectedSession.id}/seat/${selectedSeat}/change/${newSeat}`,
        {}
      );
      alert("Asiento cambiado");
    } else if (confirmAction === "DELETE") {
      await deleteData(
        `http://localhost:8080/api/tickets/session/${selectedSession.id}/seat/${selectedSeat}`
      );
      alert("Asiento liberado");
    }

    setConfirmAction(null);
    onClose();
    setNewSeat("");
    onRefresh();
  };

  // No renderiza el modal si no está abierto
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-lg p-6 w-96 text-center">
        <h3 className="text-xl font-bold mb-4">Información del asiento</h3>

        <p className="mb-4 text-lg">{seatInfo}</p>

        {/* Opciones de gestión solo si el asiento está ocupado */}
        {seatInfo.includes("ocupado") && (
          <>
            <input
              type="number"
              min={1}
              max={selectedSession.room.totalSeats}
              placeholder={`Nuevo asiento (1 - ${selectedSession.room.totalSeats})`}
              className="border p-2 w-full mb-3"
              value={newSeat}
              onChange={e => setNewSeat(e.target.value)}
            />

            {!confirmAction && (
              <>
                <button
                  className="bg-yellow-500 hover:bg-yellow-600 text-white px-4 py-2 rounded w-full mb-2 transition-colors"
                  onClick={() => handleChangeOrDeleteSeat("CHANGE")}
                >
                  Cambiar asiento
                </button>

                <button
                  className="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded w-full transition-colors"
                  onClick={() => handleChangeOrDeleteSeat("DELETE")}
                >
                  Liberar asiento
                </button>
              </>
            )}

            {/* Confirmación de la acción seleccionada */}
            {confirmAction && (
              <div className="mt-4 bg-gray-100 p-4 rounded">
                <p className="mb-4 font-semibold">
                  {confirmAction === "CHANGE"
                    ? `¿Confirmar cambio al asiento ${newSeat}?`
                    : `¿Confirmar liberación del asiento ${selectedSeat}?`}
                </p>

                <div className="flex gap-3">
                  <button
                    className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded w-full transition-colors"
                    onClick={handleConfirm}
                  >
                    Confirmar
                  </button>

                  <button
                    className="bg-gray-400 hover:bg-gray-500 text-white px-4 py-2 rounded w-full transition-colors"
                    onClick={() => setConfirmAction(null)}
                  >
                    Cancelar
                  </button>
                </div>
              </div>
            )}
          </>
        )}

        {/* Cierre del modal sin realizar acciones */}
        <button
          className="mt-4 text-gray-600 hover:text-gray-800 underline transition-colors"
          onClick={() => {
            setConfirmAction(null);
            onClose();
          }}
        >
          Cerrar
        </button>
      </div>
    </div>
  );
}
