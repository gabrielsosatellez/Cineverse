package com.cineverse.cineverse.controllers;

import com.cineverse.cineverse.entities.Session;
import com.cineverse.cineverse.entities.Ticket;
import com.cineverse.cineverse.entities.User;
import com.cineverse.cineverse.repositories.SessionRepository;
import com.cineverse.cineverse.repositories.TicketRepository;
import com.cineverse.cineverse.repositories.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketRepository ticketRepository;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public TicketController(TicketRepository ticketRepository,
                            SessionRepository sessionRepository,
                            UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    /**
     * Devuelve todas las entradas asociadas a una sesión concreta.
     * Endpoint: GET /api/tickets/session/{sessionId}
     */
    @GetMapping("/session/{sessionId}")
    public List<Ticket> getBySession(@PathVariable Long sessionId) {
        Session session = sessionRepository.findById(sessionId).orElseThrow();
        return ticketRepository.findBySession(session);
    }

    /**
     * Devuelve los números de butacas ocupadas en una sesión.
     * Endpoint: GET /api/tickets/session/{sessionId}/occupied-seats
     */
    @GetMapping("/session/{sessionId}/occupied-seats")
    public List<Integer> getOccupiedSeats(@PathVariable Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow();

        return ticketRepository.findSeatNumbersBySession(session);
    }

    /**
     * Devuelve todas las entradas compradas por un usuario.
     * Endpoint: GET /api/tickets/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public List<Ticket> getUserTickets(@PathVariable Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return ticketRepository.findByUser(user);
    }

    /**
     * Devuelve el estado de un asiento concreto en una sesión.
     * Indica si está ocupado y, en caso afirmativo, el nombre del usuario.
     * Endpoint: GET /api/tickets/session/{sessionId}/seat/{seatNumber}
     */
    @GetMapping("/session/{sessionId}/seat/{seatNumber}")
    public Map<String, Object> getSeatInfo(
            @PathVariable Long sessionId,
            @PathVariable int seatNumber
    ) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow();

        Map<String, Object> response = new HashMap<>();

        ticketRepository.findBySessionAndSeatNumber(session, seatNumber)
                .ifPresentOrElse(ticket -> {
                    response.put("occupied", true);
                    response.put("username", ticket.getUser().getFullName());
                }, () -> {
                    response.put("occupied", false);
                });

        return response;
    }

    /**
     * Realiza la compra de una o varias entradas para una sesión.
     * Se valida que los asientos solicitados estén libres antes de crear los tickets.
     * Endpoint: POST /api/tickets/buy
     */
    @PostMapping("/buy")
    @Transactional
    public List<Ticket> buyTickets(
            @RequestParam Long sessionId,
            @RequestParam List<Integer> seatNumbers,
            @RequestParam String email
    ) {

        Session session = sessionRepository.findById(sessionId).orElseThrow();
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        // Comprobar que ninguno de los asientos esté ocupado
        for (Integer seat : seatNumbers) {
            if (ticketRepository.findBySessionAndSeatNumber(session, seat).isPresent()) {
                throw new RuntimeException("La butaca " + seat + " ya está ocupada");
            }
        }

        // Crear los tickets para los asientos seleccionados
        List<Ticket> tickets = new ArrayList<>();
        for (Integer seat : seatNumbers) {
            Ticket ticket = new Ticket();
            ticket.setSession(session);
            ticket.setSeatNumber(seat);
            ticket.setUser(user);
            tickets.add(ticketRepository.save(ticket));
        }

        return tickets;
    }

    /**
     * Libera (elimina) una entrada asociada a un asiento concreto de una sesión.
     * Endpoint: DELETE /api/tickets/session/{sessionId}/seat/{seatNumber}
     */
    @DeleteMapping("/session/{sessionId}/seat/{seatNumber}")
    public void freeSeat(
            @PathVariable Long sessionId,
            @PathVariable int seatNumber
    ) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow();

        Ticket ticket = ticketRepository
                .findBySessionAndSeatNumber(session, seatNumber)
                .orElseThrow(() -> new RuntimeException("Asiento no ocupado"));

        ticketRepository.delete(ticket);
    }

    /**
     * Cambia el asiento de una entrada existente por otro asiento libre.
     * Endpoint: PUT /api/tickets/session/{sessionId}/seat/{oldSeat}/change/{newSeat}
     */
    @PutMapping("/session/{sessionId}/seat/{oldSeat}/change/{newSeat}")
    public Ticket changeSeat(
            @PathVariable Long sessionId,
            @PathVariable int oldSeat,
            @PathVariable int newSeat
    ) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow();

        // Comprobar que el nuevo asiento esté libre
        if (ticketRepository.findBySessionAndSeatNumber(session, newSeat).isPresent()) {
            throw new RuntimeException("El nuevo asiento ya está ocupado");
        }

        Ticket ticket = ticketRepository
                .findBySessionAndSeatNumber(session, oldSeat)
                .orElseThrow(() -> new RuntimeException("Asiento original no existe"));

        ticket.setSeatNumber(newSeat);
        return ticketRepository.save(ticket);
    }
}