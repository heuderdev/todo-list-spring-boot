package br.com.heuderdev.todolist.models;


import jakarta.persistence.*;
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

    private Long user_id;

    private String description;

    @Column(length = 50)
    private String title;

    private LocalDateTime start_at;

    private LocalDateTime end_at;

    private String priority;

    @CreationTimestamp
    private LocalDateTime created_at;
}
