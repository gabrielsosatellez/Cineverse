package com.cineverse.cineverse.controllers;

import com.cineverse.cineverse.entities.User;
import com.cineverse.cineverse.repositories.UserRepository;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Devuelve el listado completo de usuarios registrados.
     * Endpoint: GET /api/users
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Actualiza el rol de un usuario (por ejemplo: USER, EMPLOYEE, ADMIN).
     * Endpoint: PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    public User updateUserRole(
            @PathVariable Long id,
            @RequestBody User updatedUser
    ) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setRole(updatedUser.getRole());

        return userRepository.save(user);
    }

    /**
     * Elimina un usuario por su ID.
     * Endpoint: DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        userRepository.deleteById(id);
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * Se valida que el email no esté ya registrado y se almacena la contraseña hasheada.
     * Endpoint: POST /api/users/register
     */
    @PostMapping("/register")
    public User register(@RequestBody User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Asignar rol por defecto
        user.setRole("USER");

        // Hashear la contraseña antes de guardarla en base de datos
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    /**
     * Realiza el login de un usuario mediante email y contraseña.
     * Se comprueba la contraseña utilizando BCrypt.
     * Endpoint: POST /api/users/login
     */
    @PostMapping("/login")
    public User login(@RequestBody User loginData) {

        User user = userRepository.findByEmail(loginData.getEmail());

        if (user == null ||
            !passwordEncoder.matches(loginData.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        return user;
    }
}
