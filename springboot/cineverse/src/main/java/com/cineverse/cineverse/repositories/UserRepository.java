package com.cineverse.cineverse.repositories;

import com.cineverse.cineverse.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la entidad User.
 * Proporciona operaciones CRUD y consultas personalizadas sobre los usuarios del sistema.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por su email.
     * Se utiliza principalmente en el proceso de login.
     */
    User findByEmail(String email);

    /**
     * Comprueba si existe un usuario registrado con un email concreto.
     * Se utiliza para evitar registros duplicados.
     */
    boolean existsByEmail(String email);
}