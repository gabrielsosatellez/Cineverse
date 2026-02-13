package com.cineverse.cineverse.repositories;

import com.cineverse.cineverse.entities.CinemaRoom;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la entidad CinemaRoom.
 * Proporciona operaciones CRUD para la gestión de salas de cine.
 */
public interface CinemaRoomRepository extends JpaRepository<CinemaRoom, Long> {
}