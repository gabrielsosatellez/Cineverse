package com.cineverse.cineverse.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private CinemaRoom room;

    @Column(nullable = false)
    private LocalDateTime dateTime;
    
    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal price;
    
	@OneToMany(mappedBy = "session", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Ticket> tickets;

    public Session() {
    }

    public Session(Movie movie, CinemaRoom room, LocalDateTime dateTime, BigDecimal price) {
        this.movie = movie;
        this.room = room;
        this.dateTime = dateTime;
        this.price = price;
    }

    // Getters y setters
    
    public Long getId() {
        return id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public CinemaRoom getRoom() {
        return room;
    }

    public void setRoom(CinemaRoom room) {
        this.room = room;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    
    public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	@JsonProperty("movieTitle")
	public String getMovieTitle() {
	    return movie != null ? movie.getTitle() : null;
	}

	@JsonProperty("roomName")
	public String getRoomName() {
	    return room != null ? room.getName() : null;
	}

}
