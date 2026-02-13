import React from "react";

export default function SeatGrid({ selectedSession, takenSeats, onSeatClick }) {

  // Renderiza la cuadrícula de asientos de la sala asociada a la sesión seleccionada
  return (
    <div className="flex flex-col gap-3 items-center">
      {Array.from({
        length: Math.ceil(selectedSession.room.totalSeats / 12)
      }).map((_, rowIndex) => (
        <div key={rowIndex} className="flex gap-8">

          {/* Bloque de asientos del lado izquierdo */}
          <div className="flex gap-2">
            {Array.from({ length: 6 }).map((_, seatIndex) => {
              const seatNumber = rowIndex * 12 + seatIndex + 1;
              if (seatNumber > selectedSession.room.totalSeats) return null;

              const isTaken = takenSeats.includes(seatNumber);

              return (
                <div
                  key={seatNumber}
                  onClick={() => onSeatClick(seatNumber)}
                  className={`w-11 h-11 rounded flex items-center justify-center text-sm font-bold cursor-pointer transition-colors ${
                    isTaken
                      ? "bg-red-500 text-white hover:bg-red-600"
                      : "bg-green-600 text-white hover:bg-green-700"
                  }`}
                >
                  {seatNumber}
                </div>
              );
            })}
          </div>

          {/* Separación visual del pasillo central */}
          <div className="w-8"></div>

          {/* Bloque de asientos del lado derecho */}
          <div className="flex gap-2">
            {Array.from({ length: 6 }).map((_, seatIndex) => {
              const seatNumber = rowIndex * 12 + seatIndex + 7;
              if (seatNumber > selectedSession.room.totalSeats) return null;

              const isTaken = takenSeats.includes(seatNumber);

              return (
                <div
                  key={seatNumber}
                  onClick={() => onSeatClick(seatNumber)}
                  className={`w-11 h-11 rounded flex items-center justify-center text-sm font-bold cursor-pointer transition-colors ${
                    isTaken
                      ? "bg-red-500 text-white hover:bg-red-600"
                      : "bg-green-600 text-white hover:bg-green-700"
                  }`}
                >
                  {seatNumber}
                </div>
              );
            })}
          </div>
        </div>
      ))}
    </div>
  );
}
