package com.cineverse.cineverse.repositories;

import com.cineverse.cineverse.entities.Session;
import com.cineverse.cineverse.entities.Ticket;
import com.cineverse.cineverse.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Ticket.
 * Proporciona operaciones CRUD y consultas específicas para la gestión de entradas y butacas.
 */
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    /**
     * Devuelve todas las entradas asociadas a una sesión concreta.
     */
    List<Ticket> findBySession(Session session);

    /**
     * Busca una entrada por sesión y número de asiento.
     * Se utiliza para comprobar si una butaca está ocupada.
     */
    Optional<Ticket> findBySessionAndSeatNumber(Session session, int seatNumber);

    /**
     * Devuelve la lista de números de asientos ocupados en una sesión.
     */
    @Query("SELECT t.seatNumber FROM Ticket t WHERE t.session = :session")
    List<Integer> findSeatNumbersBySession(@Param("session") Session session);

    /**
     * Devuelve todas las entradas compradas por un usuario concreto.
     */
    List<Ticket> findByUser(User user);
}