package com.cineverse.cineverse.entities;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "cinema_rooms")
public class CinemaRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int totalSeats;
    
    @OneToMany(
    	    mappedBy = "room",
    	    cascade = CascadeType.REMOVE,
    	    orphanRemoval = true
    	)
    	private List<Session> sessions;


    public CinemaRoom() {
    }

    public CinemaRoom(String name, int totalSeats) {
        this.name = name;
        this.totalSeats = totalSeats;
    }

	// Getters y setters
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public int getTotalSeats() {
        return totalSeats;
    }
    
    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }
}