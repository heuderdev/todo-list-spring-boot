package br.com.heuderdev.todolist.repositories;

import br.com.heuderdev.todolist.models.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITaskRepository extends JpaRepository<TaskModel, Long> {
}
