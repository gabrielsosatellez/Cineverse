package com.cineverse.cineverse.repositories;

import com.cineverse.cineverse.entities.Movie;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la entidad Movie.
 * Proporciona operaciones CRUD sobre las películas y consultas personalizadas.
 */
public interface MovieRepository extends JpaRepository<Movie, Long> {

    /**
     * Busca una película por su título.
     * Se utiliza para evitar duplicados al crear nuevas películas.
     */
    Optional<Movie> findByTitle(String title);
}