package com.planificador.backend_planificador_tareas.service;

import com.planificador.backend_planificador_tareas.model.Task;
import com.planificador.backend_planificador_tareas.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // GET /api/tasks - Obtener todas las tareas[cite: 1, 2]
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // GET /api/tasks/{id} - Obtener tarea por ID
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    // POST /api/tasks - Crear nueva tarea[cite: 1, 2]
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    // PUT /api/tasks/{id} - Actualizar tarea existente[cite: 1, 2]
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

    // DELETE /api/tasks/{id} - Eliminar tarea[cite: 1, 2]
    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }
}