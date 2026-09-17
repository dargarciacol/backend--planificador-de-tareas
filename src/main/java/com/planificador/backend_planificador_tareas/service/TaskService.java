package com.planificador.backend_planificador_tareas.service;

import com.planificador.backend_planificador_tareas.model.Task;
import com.planificador.backend_planificador_tareas.model.User;
import com.planificador.backend_planificador_tareas.repository.TaskRepository;
import com.planificador.backend_planificador_tareas.repository.UserRepository; // <-- Asegúrate de importar tu repositorio de usuarios
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository; // <-- Inyectamos UserRepository

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    // GET /api/tasks - Obtener todas las tareas
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // GET /api/tasks/{id} - Obtener tarea por ID
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    // POST /api/tasks - Crear nueva tarea asociada al usuario del JWT
    public Task createTask(Task task) {
        // 1. Obtener el email del usuario autenticado a través del token JWT en el contexto
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. Buscar el usuario en la base de datos
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el correo: " + email));

        // 3. Asignar el usuario obligatorio a la tarea
        task.setUser(user);

        // 4. Guardar en Supabase con el user_id correcto
        return taskRepository.save(task);
    }

    // PUT /api/tasks/{id} - Actualizar tarea existente
    public Optional<Task> updateTask(Long id, Task taskDetails) {
        return taskRepository.findById(id).map(existingTask -> {
            existingTask.setName(taskDetails.getName());
            existingTask.setDescription(taskDetails.getDescription());
            existingTask.setDueDate(taskDetails.getDueDate());
            existingTask.setStatus(taskDetails.getStatus());
            existingTask.setPriority(taskDetails.getPriority());
            return taskRepository.save(existingTask);
        });
    }

    // DELETE /api/tasks/{id} - Eliminar tarea
    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }
}