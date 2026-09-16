package com.planificador.backend_planificador_tareas.repository;

import com.planificador.backend_planificador_tareas.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}