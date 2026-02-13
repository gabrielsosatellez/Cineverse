import React, { useState } from "react";
import { postData, deleteData, fetchData } from "../../utils/api";

export default function MoviesSection({
  movies,
  setMovies,
  isAdmin,
  sessions,
  setSessions,
  selectedSession,
  setSelectedSession,
  setTakenSeats
}) {
  const [newMovieTitle, setNewMovieTitle] = useState("");
  const [newMovieDescription, setNewMovieDescription] = useState("");
  const [newMovieDuration, setNewMovieDuration] = useState("");
  const [newMovieImageUrl, setNewMovieImageUrl] = useState("");
  const [showConfirm, setShowConfirm] = useState(false);
  const [movieToDelete, setMovieToDelete] = useState(null);

  // Crea una nueva película en el backend y recarga el listado
  const handleAddMovie = (e) => {
    e.preventDefault();
    postData("http://localhost:8080/api/movies", {
      title: newMovieTitle,
      description: newMovieDescription,
      duration: newMovieDuration,
      imageUrl: newMovieImageUrl
    })
      .then(res => {
        if (!res.ok) throw new Error("La película ya existe");
      })
      .then(() => {
        alert("Película añadida correctamente");
        setNewMovieTitle("");
        setNewMovieDescription("");
        setNewMovieDuration("");
        setNewMovieImageUrl("");
        return fetchData("http://localhost:8080/api/movies");
      })
      .then(setMovies)
      .catch(err => alert(err.message));
  };

  // Elimina una película y actualiza películas y sesiones asociadas
  const handleDeleteMovie = () => {
    deleteData(`http://localhost:8080/api/movies/${movieToDelete.id}`)
      .then(() => {
        // Recarga el listado de películas y sesiones tras la eliminación
        return Promise.all([
          fetchData("http://localhost:8080/api/movies"),
          fetchData("http://localhost:8080/api/sessions")
        ]);
      })
      .then(([moviesData, sessionsData]) => {
        setMovies(moviesData);
        setSessions(sessionsData);

        // Limpia la sesión seleccionada si pertenecía a la película eliminada
        if (selectedSession && !sessionsData.find(s => s.id === selectedSession.id)) {
          setSelectedSession(null);
          setTakenSeats([]);
        }

        setShowConfirm(false);
        setMovieToDelete(null);
      });
  };

  return (
    <>
      <h2 className="text-2xl font-bold mb-4">Películas</h2>

      {/* Formulario de alta de películas (solo visible para administradores) */}
      {isAdmin && (
        <form
          className="bg-white p-4 rounded shadow mb-6"
          onSubmit={handleAddMovie}
        >
          <h3 className="font-bold mb-2">Añadir película</h3>

          <input
            className="border p-2 w-full mb-2"
            placeholder="Título"
            value={newMovieTitle}
            onChange={e => setNewMovieTitle(e.target.value)}
          />

          <input
            className="border p-2 w-full mb-2"
            type="number"
            placeholder="Duración"
            value={newMovieDuration}
            onChange={e => setNewMovieDuration(e.target.value)}
          />

          <input
            className="border p-2 w-full mb-2"
            placeholder="URL imagen"
            value={newMovieImageUrl}
            onChange={e => setNewMovieImageUrl(e.target.value)}
          />

          <textarea
            className="border p-2 w-full mb-2"
            placeholder="Descripción"
            value={newMovieDescription}
            onChange={e => setNewMovieDescription(e.target.value)}
          />

          <button className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded transition-colors">
            Añadir película
          </button>
        </form>
      )}

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {movies.map(movie => (
          <div key={movie.id} className="bg-white rounded shadow overflow-hidden">
            {movie.imageUrl && (
              <img
                src={movie.imageUrl || `${process.env.PUBLIC_URL}/no-poster.png`}
                alt={movie.title}
                onError={(e) => {
                  // Fallback a imagen por defecto si falla la carga del póster
                  e.currentTarget.onerror = null;
                  e.currentTarget.src = `${process.env.PUBLIC_URL}/no-poster.png`;
                }}
                className="w-full h-48 object-cover"
              />
            )}
            <div className="p-4">
              <h3 className="text-xl font-bold">{movie.title}</h3>
              <p className="text-sm text-gray-600">{movie.duration} min</p>
              <p className="mt-2">{movie.description}</p>

              {/* Acción de eliminación de película (solo para administradores) */}
              {isAdmin && (
                <button
                  className="bg-red-500 hover:bg-red-600 text-white px-3 py-1 rounded mt-3 transition-colors"
                  onClick={() => {
                    setMovieToDelete(movie);
                    setShowConfirm(true);
                  }}
                >
                  Eliminar
                </button>
              )}
            </div>
          </div>
        ))}
      </div>

      {/* Modal de confirmación para la eliminación de películas */}
      {showConfirm && movieToDelete && (
        <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-lg p-6 w-96 text-center">
            <h3 className="text-xl font-bold mb-4">Eliminar película</h3>
            <p className="mb-6">
              ¿Seguro que quieres eliminar<br />
              <span className="font-semibold">{movieToDelete.title}</span>?
            </p>
            <div className="flex gap-4">
              <button
                className="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded w-full transition-colors"
                onClick={handleDeleteMovie}
              >
                Eliminar
              </button>
              <button
                className="bg-gray-400 hover:bg-gray-500 text-white px-4 py-2 rounded w-full transition-colors"
                onClick={() => {
                  setShowConfirm(false);
                  setMovieToDelete(null);
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
