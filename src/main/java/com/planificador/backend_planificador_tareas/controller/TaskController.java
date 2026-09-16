package com.planificador.backend_planificador_tareas.controller;

import com.planificador.backend_planificador_tareas.dto.TaskDTORequest;
import com.planificador.backend_planificador_tareas.dto.TaskDTOResponse;
import com.planificador.backend_planificador_tareas.model.Task;
import com.planificador.backend_planificador_tareas.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
@Tag(name = "Tareas", description = "Endpoints para la gestión y administración del planificador de tareas")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Métodos auxiliares de mapeo entre Entidad y DTO
    private TaskDTOResponse convertToDTO(Task task) {
        return new TaskDTOResponse(
                task.getId(),
                task.getName(),
                task.getDescription(),
                task.getDueDate(),
                task.getStatus(),
                task.getPriority()
        );
    }

    private Task convertToEntity(TaskDTORequest dto) {
        return new Task(
                dto.getName(),
                dto.getDescription(),
                dto.getDueDate(),
                dto.getStatus(),
                dto.getPriority()
        );
    }

    // 1. GET /api/tasks - Recupera todas las tareas
    @Operation(summary = "Obtener todas las tareas", description = "Devuelve una lista completa de todas las tareas registradas en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tareas recuperada exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<TaskDTOResponse>> getAllTasks() {
        List<TaskDTOResponse> tasks = taskService.getAllTasks()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    // 2. POST /api/tasks - Crea una nueva tarea
    @Operation(summary = "Crear una nueva tarea", description = "Crea una nueva tarea en la base de datos a partir de los datos validados recibidos en el cuerpo de la petición.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tarea creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o faltantes")
    })
    @PostMapping
    public ResponseEntity<TaskDTOResponse> createTask(@Valid @RequestBody TaskDTORequest taskDTORequest) {
        Task taskToCreate = convertToEntity(taskDTORequest);
        Task createdTask = taskService.createTask(taskToCreate);
        return new ResponseEntity<>(convertToDTO(createdTask), HttpStatus.CREATED);
    }

    // 3. PUT /api/tasks/{id} - Actualiza una tarea por su ID
    @Operation(summary = "Actualizar una tarea existente", description = "Actualiza los campos de una tarea identificada por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarea actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "No se encontró la tarea con el ID proporcionado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTOResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskDTORequest taskDTORequest) {

        Task taskDetails = convertToEntity(taskDTORequest);
        return taskService.updateTask(id, taskDetails)
                .map(updatedTask -> ResponseEntity.ok(convertToDTO(updatedTask)))
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. DELETE /api/tasks/{id} - Elimina una tarea por su ID
    @Operation(summary = "Eliminar una tarea", description = "Elimina permanentemente una tarea de la base de datos según su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tarea eliminada exitosamente (Sin contenido)"),
            @ApiResponse(responseCode = "404", description = "No se encontró la tarea con el ID proporcionado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        boolean deleted = taskService.deleteTask(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}