package com.planificador.backend_planificador_tareas.repository;

import com.planificador.backend_planificador_tareas.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por su correo electrónico (case insensitive).
     */
    Optional<User> findByEmail(String email);

    /**
     * Comprueba si un usuario ya existe con ese correo electrónico.
     */
    boolean existsByEmail(String email);
}