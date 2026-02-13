import React, { useState } from "react";
import { postData, fetchData, movieExists } from "../../utils/api";

export default function StarWarsSection({ movies, setMovies }) {
  const [swMovies, setSwMovies] = useState([]);
  const [loadingSw, setLoadingSw] = useState(false);

  // Carga las películas de Star Wars desde el backend (integración con SWAPI)
  const handleLoadStarWars = () => {
    setLoadingSw(true);
    fetchData("http://localhost:8080/api/swapi/movies")
      .then(data => {
        console.log("SWAPI DATA PARSED:", data);
        setSwMovies(Array.isArray(data) ? data : []);
        setLoadingSw(false);
      })
      .catch(err => {
        console.error(err);
        setSwMovies([]);
        setLoadingSw(false);
      });
  };

  // Importa una película de Star Wars al catálogo de CineVerse
  const handleImportMovie = (movie) => {
    postData("http://localhost:8080/api/swapi/import", movie)
      .then(res => {
        if (!res.ok) throw new Error("La película ya existe");
      })
      .then(() => {
        alert("Película importada a CineVerse");
        return fetchData("http://localhost:8080/api/movies");
      })
      .then(setMovies)
      .catch(err => alert(err.message));
  };

  return (
    <>
      <h2 className="text-2xl font-bold mb-4">Campaña Star Wars</h2>

      <button
        className="bg-yellow-500 hover:bg-yellow-600 text-black px-4 py-2 rounded mb-4 transition-colors font-semibold"
        onClick={handleLoadStarWars}
      >
        Cargar películas de Star Wars
      </button>

      {loadingSw && <p>Cargando películas...</p>}

      <ul>
        {Array.isArray(swMovies) && swMovies.map(movie => {
          // URL del póster obtenida desde la API externa
          const apiPoster = `https://starwars-visualguide.com/assets/img/films/${movie.episode_id}.jpg`;

          // Mapeo de pósters locales por título como respaldo en caso de error
          const localStarWarsPosters = {
            "The Phantom Menace": `${process.env.PUBLIC_URL}/starwars/4.jpg`,
            "Attack of the Clones": `${process.env.PUBLIC_URL}/starwars/5.jpg`,
            "Revenge of the Sith": `${process.env.PUBLIC_URL}/starwars/6.jpg`,
            "A New Hope": `${process.env.PUBLIC_URL}/starwars/1.webp`,
            "The Empire Strikes Back": `${process.env.PUBLIC_URL}/starwars/2.jpg`,
            "Return of the Jedi": `${process.env.PUBLIC_URL}/starwars/3.jpg`
          };

          const localPoster =
            localStarWarsPosters[movie.title] ||
            `${process.env.PUBLIC_URL}/no-poster.png`;

          return (
            <li
              key={movie.title}
              className="bg-white p-3 mb-2 rounded shadow flex justify-between items-center"
            >
              <div className="flex items-center gap-4">
                <img
                  src={apiPoster}
                  alt={movie.title}
                  className="w-24 h-36 object-cover rounded-md"
                  onError={(e) => {
                    // Fallback al póster local si falla la carga del póster externo
                    e.currentTarget.onerror = null;
                    e.currentTarget.src = localPoster;
                  }}
                />
                <div>
                  <p className="font-bold">{movie.title}</p>
                  <p className="text-sm text-gray-600">
                    Estreno: {movie.release_date}
                  </p>
                </div>
              </div>

              <button
                disabled={movieExists(movie.title, movies)}
                className={`px-3 py-1 rounded text-white transition-colors ${
                  movieExists(movie.title, movies)
                    ? "bg-gray-400 cursor-not-allowed"
                    : "bg-blue-600 hover:bg-blue-700"
                }`}
                onClick={() => handleImportMovie(movie)}
              >
                {movieExists(movie.title, movies) ? "Ya importada" : "Importar"}
              </button>
            </li>
          );
        })}
      </ul>
    </>
  );
}
