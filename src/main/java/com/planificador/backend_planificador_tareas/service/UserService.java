package com.planificador.backend_planificador_tareas.service;

import com.planificador.backend_planificador_tareas.model.User;
import com.planificador.backend_planificador_tareas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * Valida que el email no esté en uso.
     */
    @Transactional
    public User registerUser(User user) {
        String normalizedEmail = user.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }

        user.setEmail(normalizedEmail);
        // NOTA: Si vas a integrar Spring Security más adelante, aquí encriptas la clave:
        // user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    /**
     * Valida las credenciales de inicio de sesión.
     */
    @Transactional(readOnly = true)
    public Optional<User> authenticateUser(String email, String password) {
        String normalizedEmail = email.trim().toLowerCase();

        return userRepository.findByEmail(normalizedEmail)
                .filter(user -> user.getPassword().equals(password));
        // NOTA: Con Spring Security se usa: passwordEncoder.matches(password, user.getPassword())
    }

    /**
     * Obtiene un usuario por su ID.
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Obtiene un usuario por su Email.
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email.trim().toLowerCase());
    }

    /**
     * Lista todos los usuarios registrados.
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Elimina un usuario por su ID.
     */
    @Transactional
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}