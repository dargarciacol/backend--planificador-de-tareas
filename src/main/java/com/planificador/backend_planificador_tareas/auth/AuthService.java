package com.planificador.backend_planificador_tareas.auth;

import com.planificador.backend_planificador_tareas.DTO.LoginRequestDTO;
import com.planificador.backend_planificador_tareas.DTO.LoginResponseDTO;
import com.planificador.backend_planificador_tareas.DTO.RegisterRequestDTO;
import com.planificador.backend_planificador_tareas.DTO.RegisterResponseDTO;
import com.planificador.backend_planificador_tareas.model.User;
import com.planificador.backend_planificador_tareas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Procesa el registro de un nuevo usuario
     */
    @Transactional
    public RegisterResponseDTO register(RegisterRequestDTO request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }

        User newUser = new User(
                request.getName(),
                normalizedEmail,
                request.getPassword()
        );

        User savedUser = userRepository.save(newUser);

        // Generación de token de sesión (o JWT)
        String token = "auth_token_" + UUID.randomUUID().toString();

        return new RegisterResponseDTO(
                "Usuario registrado exitosamente",
                token,
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail()
        );
    }

    /**
     * Procesa la autenticación e inicio de sesión
     */
    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();

        Optional<User> userOpt = userRepository.findByEmail(normalizedEmail);

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(request.getPassword())) {
            User user = userOpt.get();
            String token = "auth_token_" + UUID.randomUUID().toString();

            return new LoginResponseDTO(
                    "Inicio de sesión exitoso",
                    token,
                    user.getId(),
                    user.getName(),
                    user.getEmail()
            );
        } else {
            throw new IllegalArgumentException("Correo electrónico o contraseña incorrectos");
        }
    }
}