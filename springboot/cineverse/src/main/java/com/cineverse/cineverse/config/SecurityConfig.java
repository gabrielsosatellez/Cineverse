package com.cineverse.cineverse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /**
     * Configura la cadena de filtros de seguridad de Spring Security.
     * En este proyecto se desactiva CSRF y se permite el acceso a todos los endpoints
     * para facilitar el desarrollo y las pruebas.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Se desactiva la protección CSRF (útil para APIs REST consumidas por frontend externo)
            .csrf(csrf -> csrf.disable())

            // Se permite el acceso a cualquier endpoint sin autenticación
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );

        return http.build();
    }
}
