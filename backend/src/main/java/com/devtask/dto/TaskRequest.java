package com.devtask.dto;
import com.devtask.model.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record TaskRequest(@NotBlank @Size(max=160) String title, @Size(max=2000) String description, Priority priority,
                          TaskStatus status, LocalDate deadline, Long assignedToId, @NotNull Long projectId) {}
