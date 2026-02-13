package com.cineverse.cineverse.controllers;

import com.cineverse.cineverse.entities.Movie;
import com.cineverse.cineverse.repositories.MovieRepository;
import com.cineverse.cineverse.repositories.SessionRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieRepository movieRepository;
    private final SessionRepository sessionRepository;

    public MovieController(MovieRepository movieRepository,
                           SessionRepository sessionRepository) {
        this.movieRepository = movieRepository;
        this.sessionRepository = sessionRepository;
    }

    /**
     * Obtiene la cartelera completa de películas.
     * Endpoint: GET /api/movies
     */
    @GetMapping
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    /**
     * Crea una nueva película en el sistema.
     * Se comprueba previamente que no exista otra película con el mismo título.
     * Endpoint: POST /api/movies
     */
    @PostMapping
    public Movie createMovie(@RequestBody Movie movie) {
        if (movieRepository.findByTitle(movie.getTitle()).isPresent()) {
            throw new RuntimeException("La película ya existe");
        }

        return movieRepository.save(movie);
    }

    /**
     * Elimina una película por su ID.
     * Antes de borrar la película, se eliminan las sesiones asociadas para evitar inconsistencias.
     * Endpoint: DELETE /api/movies/{id}
     */
    @DeleteMapping("/{id}")
    @Transactional
    public void deleteMovie(@PathVariable Long id) {

        // Eliminar primero las sesiones asociadas a la película
        sessionRepository.deleteByMovieId(id);

        // Eliminar la película
        movieRepository.deleteById(id);
    }
}
