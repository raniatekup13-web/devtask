package com.devtask.dto;

import com.devtask.model.Priority;
import com.devtask.model.TaskStatus;
import java.time.LocalDate;

public record TaskResponse(Long id, String title, String description, Priority priority, TaskStatus status,
                           LocalDate deadline, Long projectId, String projectName,
                           Long assignedToId, String assignedToEmail) {}
