package br.com.heuderdev.todolist.controllers;


import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.heuderdev.todolist.models.TaskModel;
import br.com.heuderdev.todolist.models.UserModel;
import br.com.heuderdev.todolist.repositories.ITaskRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private ITaskRepository repository;

    @PostMapping
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {

        if (taskModel.getTitle().length() > 50) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{O titulo tem mais que 50 caracteres}");
        }

        var user_id = request.getAttribute("user_id");

        taskModel.setUser_id((Long) user_id);

        var currentDate = LocalDateTime.now();

        if(currentDate.isAfter(taskModel.getStart_at()) || currentDate.isAfter(taskModel.getEnd_at())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio deve ser maior que a data atual");
        }

        var taskCreated = this.repository.save(taskModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskCreated);
    }
}
