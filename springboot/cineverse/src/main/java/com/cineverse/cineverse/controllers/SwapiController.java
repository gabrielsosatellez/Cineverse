package com.cineverse.cineverse.controllers;

import com.cineverse.cineverse.entities.Movie;
import com.cineverse.cineverse.services.SwapiService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/swapi")
@CrossOrigin(origins = "http://localhost:3000")
public class SwapiController {

    private final SwapiService swapiService;

    public SwapiController(SwapiService swapiService) {
        this.swapiService = swapiService;
    }

    /**
     * Obtiene el listado de películas de Star Wars desde la API externa (SWAPI).
     * Este endpoint se utiliza en la web para mostrar las películas disponibles para importar.
     * Endpoint: GET /api/swapi/movies
     */
    @GetMapping("/movies")
    public List<Map<String, Object>> getStarWarsMovies() {
        return swapiService.getAllStarWarsMovies();
    }

    /**
     * Importa una película de Star Wars al sistema de CineVerse.
     * Recibe los datos de la película desde la web y los guarda en la base de datos.
     * Endpoint: POST /api/swapi/import
     */
    @PostMapping("/import")
    public Movie importMovie(@RequestBody Map<String, Object> swMovie) {
        return swapiService.importStarWarsMovie(swMovie);
    }
}

