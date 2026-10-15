package com.devtask.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter @Setter
@Entity
public class Task {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.MEDIUM;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.TODO;

    private LocalDate deadline;

    @ManyToOne(fetch=FetchType.LAZY)
    private User assignedTo;

    @ManyToOne(fetch=FetchType.LAZY)
    private Project project;
}
