package com.planificador.backend_planificador_tareas.auth;

import com.planificador.backend_planificador_tareas.DTO.LoginRequestDTO;
import com.planificador.backend_planificador_tareas.DTO.LoginResponseDTO;
import com.planificador.backend_planificador_tareas.DTO.RegisterRequestDTO;
import com.planificador.backend_planificador_tareas.DTO.RegisterResponseDTO;
import com.planificador.backend_planificador_tareas.model.User;
import com.planificador.backend_planificador_tareas.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * POST /api/auth/register - Registro de nuevo usuario
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        try {
            User newUser = new User(
                    registerRequest.getName(),
                    registerRequest.getEmail(),
                    registerRequest.getPassword()
            );

            User savedUser = userService.registerUser(newUser);

            String token = "session_token_" + UUID.randomUUID().toString();

            RegisterResponseDTO response = new RegisterResponseDTO(
                    "Usuario registrado exitosamente",
                    token,
                    savedUser.getId(),
                    savedUser.getName(),
                    savedUser.getEmail()
            );

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar el registro: " + e.getMessage());
        }
    }

    /**
     * POST /api/auth/login - Inicio de sesión
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        Optional<User> userOptional = userService.authenticateUser(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String token = "session_token_" + UUID.randomUUID().toString();

            LoginResponseDTO response = new LoginResponseDTO(
                    "Inicio de sesión exitoso",
                    token,
                    user.getId(),
                    user.getName(),
                    user.getEmail()
            );

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Correo electrónico o contraseña incorrectos");
        }
    }

    /**
     * POST /api/auth/logout - Cierre de sesión
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        return ResponseEntity.ok("Sesión cerrada correctamente");
    }
}