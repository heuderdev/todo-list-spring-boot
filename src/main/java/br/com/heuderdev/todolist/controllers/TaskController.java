package br.com.heuderdev.todolist.controllers;


import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.heuderdev.todolist.TodolistApplication;
import br.com.heuderdev.todolist.models.TaskModel;
import br.com.heuderdev.todolist.models.UserModel;
import br.com.heuderdev.todolist.repositories.ITaskRepository;
import br.com.heuderdev.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TodolistApplication.class);
    @Autowired
    private ITaskRepository repository;

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody TaskModel taskModel, HttpServletRequest request) {

        var user_id = request.getAttribute("user_id");

        taskModel.setUserId((Long) user_id);


        var currentDate = LocalDateTime.now();

        if(taskModel.getStart_at().isAfter(taskModel.getEnd_at())) {
            logger.error("A data de inicio deve ser menor que a data de termino.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio deve ser menor que a data de termino.");
        }

        var taskCreated = this.repository.save(taskModel);
        logger.info("A task: "+ taskModel.getTitle()+" foi criada com sucesso!");
        return ResponseEntity.status(HttpStatus.CREATED).body(taskCreated);
    }

    @GetMapping
    public ResponseEntity list(HttpServletRequest request) {
        var user_id = request.getAttribute("user_id");
        var taskList = this.repository.findByUserId((Long) user_id);
        return ResponseEntity.status(HttpStatus.OK).body(taskList);
    }

    @PatchMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, @PathVariable Long id, HttpServletRequest request) {

        var user_id = request.getAttribute("user_id");

        var task = this.repository.findById(id).orElse(null);

        if(task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada!");
        }

        if(!task.getUserId().equals(user_id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não tem permissão para alterar essa tarefa.");
        }

        Utils.copyNonNullProperties(taskModel, task);
        var newTask =  this.repository.save(task);
        return ResponseEntity.status(HttpStatus.OK).body(newTask);

    }
}
