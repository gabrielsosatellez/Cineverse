package com.cineverse.cineverse.repositories;

import com.cineverse.cineverse.entities.CinemaRoom;
import com.cineverse.cineverse.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Session.
 * Proporciona operaciones CRUD y consultas personalizadas sobre las sesiones de cine.
 */
public interface SessionRepository extends JpaRepository<Session, Long> {

    /**
     * Busca una sesión por sala y fecha/hora.
     * Se utiliza para evitar solapamientos de sesiones en la misma sala.
     */
    Optional<Session> findByRoomAndDateTime(CinemaRoom room, LocalDateTime dateTime);

    /**
     * Elimina todas las sesiones asociadas a una película concreta.
     */
    void deleteByMovieId(Long movieId);

    /**
     * Elimina todas las sesiones asociadas a una sala concreta.
     */
    void deleteByRoomId(Long roomId);

    /**
     * Devuelve las sesiones asociadas a una película concreta.
     */
    List<Session> findByMovieId(Long movieId);

    /**
     * Devuelve todas las sesiones ordenadas por fecha y hora ascendente.
     */
    List<Session> findAllByOrderByDateTimeAsc();

    /**
     * Busca una sesión por su ID.
     */
    Optional<Session> findById(Long id);
}
