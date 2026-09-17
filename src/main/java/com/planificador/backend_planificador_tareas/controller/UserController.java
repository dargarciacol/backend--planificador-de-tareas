package com.planificador.backend_planificador_tareas.controller;

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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * POST /api/users/register - Registro de nuevo usuario
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        try {
            User newUser = new User(
                    registerRequest.getName(),
                    registerRequest.getEmail(),
                    registerRequest.getPassword()
            );

            User savedUser = userService.registerUser(newUser);

            String token = "session_token_" + UUID.randomUUID().toString();

            RegisterResponseDTO response = new RegisterResponseDTO(
                    "Usuario registrado con éxito",
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
     * POST /api/users/login - Autenticación e inicio de sesión
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
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
     * GET /api/users - Obtener todos los usuarios
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * GET /api/users/{id} - Obtener usuario por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> userOptional = userService.getUserById(id);

        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario no encontrado con ID: " + id);
        }
    }

    /**
     * DELETE /api/users/{id} - Eliminar un usuario
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);

        if (deleted) {
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario no encontrado");
        }
    }
}