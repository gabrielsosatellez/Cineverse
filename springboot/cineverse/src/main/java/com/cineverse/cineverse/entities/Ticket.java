package com.cineverse.cineverse.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tickets",
       uniqueConstraints = @UniqueConstraint(columnNames = {"session_id", "seatNumber"}))
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @Column(nullable = false)
    private int seatNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Ticket() {
    }

    public Ticket(Session session, int seatNumber, User user) {
        this.session = session;
        this.seatNumber = seatNumber;
        this.user = user;
    }

    // Getters y setters
    
    public Long getId() {
        return id;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
}