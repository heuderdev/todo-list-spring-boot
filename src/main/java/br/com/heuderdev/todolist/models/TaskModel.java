package br.com.heuderdev.todolist.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Table(name = "tasks")
@Entity(name = "tasks")
public class TaskModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userId")
    private Long userId;

    @NotNull
    @NotEmpty
    private String description;

    @Column(length = 50)
    @Size(max = 50, min = 1)
    @NotNull
    @NotEmpty
    private String title;

    private LocalDateTime start_at;


    private LocalDateTime end_at;

    @NotNull
    @NotEmpty
    private String priority;

    @CreationTimestamp
    private LocalDateTime created_at;
}
