package com.cineverse.cineverse.entities;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    private int duration;

    @Column(name = "image_url")
    private String imageUrl;
    
    @OneToMany(mappedBy = "movie", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Session> sessions;

    public Movie() {
    }

    public Movie(String title, String description, int duration, String imageUrl) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.imageUrl = imageUrl;
    }

    // Getters y setters
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }
    
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}