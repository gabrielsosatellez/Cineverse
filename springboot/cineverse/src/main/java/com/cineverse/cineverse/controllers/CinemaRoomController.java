package com.cineverse.cineverse.controllers;

import com.cineverse.cineverse.entities.CinemaRoom;
import com.cineverse.cineverse.repositories.CinemaRoomRepository;
import com.cineverse.cineverse.repositories.SessionRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class CinemaRoomController {

    private final CinemaRoomRepository roomRepository;
    private final SessionRepository sessionRepository;

    public CinemaRoomController(CinemaRoomRepository roomRepository,
                                SessionRepository sessionRepository) {
        this.roomRepository = roomRepository;
        this.sessionRepository = sessionRepository;
    }

    /**
     * Obtiene la lista de todas las salas de cine registradas.
     * Endpoint: GET /api/rooms
     */
    @GetMapping
    public List<CinemaRoom> getAllRooms() {
        return roomRepository.findAll();
    }

    /**
     * Crea una nueva sala de cine.
     * Endpoint: POST /api/rooms
     */
    @PostMapping
    public CinemaRoom createRoom(@RequestBody CinemaRoom room) {
        return roomRepository.save(room);
    }

    /**
     * Elimina una sala de cine por su ID.
     * Antes de borrar la sala, se eliminan las sesiones asociadas para mantener la integridad de datos.
     * Endpoint: DELETE /api/rooms/{id}
     */
    @DeleteMapping("/{id}")
    @Transactional
    public void deleteRoom(@PathVariable Long id) {

        // Eliminar primero las sesiones asociadas a la sala
        sessionRepository.deleteByRoomId(id);

        // Eliminar la sala
        roomRepository.deleteById(id);
    }
}

