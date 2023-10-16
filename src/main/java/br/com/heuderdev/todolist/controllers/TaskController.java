package br.com.heuderdev.todolist.controllers;


import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.heuderdev.todolist.models.TaskModel;
import br.com.heuderdev.todolist.models.UserModel;
import br.com.heuderdev.todolist.repositories.ITaskRepository;
import br.com.heuderdev.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private ITaskRepository repository;

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody TaskModel taskModel, HttpServletRequest request) {

        if (taskModel.getTitle().length() > 50) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{O titulo tem mais que 50 caracteres}");
        }

        var user_id = request.getAttribute("user_id");

        taskModel.setUserId((Long) user_id);

        var currentDate = LocalDateTime.now();

        if(taskModel.getStart_at().isAfter(taskModel.getEnd_at())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio deve ser menor que a data de termino.");
        }

        var taskCreated = this.repository.save(taskModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskCreated);
    }

    @GetMapping
    public ResponseEntity list(HttpServletRequest request) {
        var user_id = request.getAttribute("user_id");
        var taskList = this.repository.findByUserId((Long) user_id);
        return ResponseEntity.status(HttpStatus.OK).body(taskList);
    }

    @PatchMapping("/{id}")
    public TaskModel update(@RequestBody TaskModel taskModel, @PathVariable Long id, HttpServletRequest request) {

        var task = this.repository.findById(id).orElse(null);

        Utils.copyNonNullProperties(taskModel, task);

        return this.repository.save(task);

    }
}
