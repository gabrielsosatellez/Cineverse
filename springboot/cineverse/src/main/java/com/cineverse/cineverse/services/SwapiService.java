package com.cineverse.cineverse.services;

import com.cineverse.cineverse.entities.Movie;
import com.cineverse.cineverse.repositories.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class SwapiService {

    // URL base de la API externa SWAPI para obtener las películas de Star Wars
    private static final String SWAPI_FILMS = "https://swapi.dev/api/films/";

    private final MovieRepository movieRepository;

    public SwapiService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /**
     * Obtiene el listado de películas de Star Wars desde la API externa SWAPI.
     * Devuelve los resultados en formato genérico para su consumo en la web.
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getAllStarWarsMovies() {
        RestTemplate restTemplate = new RestTemplate();

        Map<?, ?> response = restTemplate.getForObject(SWAPI_FILMS, Map.class);
        return (List<Map<String, Object>>) response.get("results");
    }

    /**
     * Importa una película de Star Wars en la base de datos de CineVerse.
     * Se comprueba previamente que la película no exista para evitar duplicados.
     */
    public Movie importStarWarsMovie(Map<String, Object> swMovie) {

        String title = (String) swMovie.get("title");
        if (movieRepository.findByTitle(title).isPresent()) {
            throw new RuntimeException("La película ya está importada");
        }

        String description = (String) swMovie.get("opening_crawl");
        int duration = 120;

        Integer episodeId = (Integer) swMovie.get("episode_id");
        String imageUrl =
            "https://starwars-visualguide.com/assets/img/films/" + episodeId + ".jpg";

        Movie movie = new Movie(title, description, duration, imageUrl);
        return movieRepository.save(movie);
    }
}