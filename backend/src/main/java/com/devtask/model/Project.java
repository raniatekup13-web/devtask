package com.devtask.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.*;

@Getter @Setter
@Entity
public class Project {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String name;
    private String description;

    @ManyToOne(fetch=FetchType.LAZY)
    private User owner;

    @OneToMany(mappedBy="project", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Task> tasks = new ArrayList<>();
}
