package com.cineverse.cineverse.controllers;

import com.cineverse.cineverse.entities.Movie;
import com.cineverse.cineverse.entities.CinemaRoom;
import com.cineverse.cineverse.entities.Session;
import com.cineverse.cineverse.repositories.MovieRepository;
import com.cineverse.cineverse.repositories.CinemaRoomRepository;
import com.cineverse.cineverse.repositories.SessionRepository;

import jakarta.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionRepository sessionRepository;
    private final MovieRepository movieRepository;
    private final CinemaRoomRepository roomRepository;

    public SessionController(SessionRepository sessionRepository,
                             MovieRepository movieRepository,
                             CinemaRoomRepository roomRepository) {
        this.sessionRepository = sessionRepository;
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
    }

    /**
     * Obtiene una sesión concreta por su ID.
     * Endpoint: GET /api/sessions/{id}
     */
    @GetMapping("/{id}")
    public Session getById(@PathVariable Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));
    }

    /**
     * Devuelve la sala asociada a una sesión.
     * Endpoint: GET /api/sessions/{id}/room
     */
    @GetMapping("/{id}/room")
    public ResponseEntity<?> getRoomBySession(@PathVariable Long id) {

        Session session = sessionRepository.findById(id)
                .orElse(null);

        if (session == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Sesión no encontrada");
        }

        if (session.getRoom() == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("La sesión no tiene sala asignada");
        }

        return ResponseEntity.ok(session.getRoom());
    }

    /**
     * Devuelve el listado de todas las sesiones ordenadas por fecha y hora.
     * Endpoint: GET /api/sessions
     */
    @GetMapping
    public List<Session> getAll() {
        return sessionRepository.findAllByOrderByDateTimeAsc();
    }

    /**
     * Devuelve las sesiones asociadas a una película concreta.
     * Endpoint: GET /api/sessions/movie/{movieId}
     */
    @GetMapping("/movie/{movieId}")
    public List<Session> getSessionsByMovie(@PathVariable Long movieId) {
        return sessionRepository.findByMovieId(movieId);
    }

    /**
     * Crea una nueva sesión para una película en una sala concreta.
     * Se valida que la sala no tenga ya una sesión en el mismo horario.
     * Endpoint: POST /api/sessions
     */
    @PostMapping
    public Session create(@RequestParam Long movieId,
                          @RequestParam Long roomId,
                          @RequestParam String dateTime,
                          @RequestParam BigDecimal price) {

        Movie movie = movieRepository.findById(movieId).orElseThrow();
        CinemaRoom room = roomRepository.findById(roomId).orElseThrow();

        LocalDateTime sessionDateTime = LocalDateTime.parse(dateTime);

        if (sessionRepository.findByRoomAndDateTime(room, sessionDateTime).isPresent()) {
            throw new RuntimeException("La sala ya tiene una sesión en ese horario");
        }

        Session session = new Session();
        session.setMovie(movie);
        session.setRoom(room);
        session.setDateTime(sessionDateTime);
        session.setPrice(price);

        return sessionRepository.save(session);
    }

    /**
     * Elimina una sesión por su ID.
     * Endpoint: DELETE /api/sessions/{id}
     */
    @DeleteMapping("/{id}")
    @Transactional
    public void deleteSession(@PathVariable Long id) {
        sessionRepository.deleteById(id);
    }
}

