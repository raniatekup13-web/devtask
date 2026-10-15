package com.devtask.service;

import com.devtask.exception.ResourceNotFoundException;
import com.devtask.dto.TaskRequest;
import com.devtask.dto.TaskResponse;
import com.devtask.model.*;
import com.devtask.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class TaskService {
    private final TaskRepository tasks; private final ProjectRepository projects; private final UserRepository users;
    public TaskService(TaskRepository t,ProjectRepository p,UserRepository u){tasks=t;projects=p;users=u;}
    private TaskResponse view(Task t){return new TaskResponse(t.getId(),t.getTitle(),t.getDescription(),t.getPriority(),t.getStatus(),t.getDeadline(),t.getProject().getId(),t.getProject().getName(),t.getAssignedTo()==null?null:t.getAssignedTo().getId(),t.getAssignedTo()==null?null:t.getAssignedTo().getEmail());}
    public List<TaskResponse> all(User actor){List<Task> list=actor.getRole()==Role.ADMIN?tasks.findAll():tasks.findByAssignedToId(actor.getId());return list.stream().map(this::view).toList();}
    public TaskResponse get(Long id,User actor){Task t=tasks.findById(id).orElseThrow(()->new ResourceNotFoundException("Task not found"));if(actor.getRole()!=Role.ADMIN&&(t.getAssignedTo()==null||!t.getAssignedTo().getId().equals(actor.getId())))throw new org.springframework.security.access.AccessDeniedException("You can only view your assigned tasks");return view(t);}
    private User assignee(Long id){if(id==null)return null;User target=users.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found"));if(target.getRole()!=Role.DEVELOPER)throw new IllegalArgumentException("Tasks can only be assigned to developer accounts");return target;}
    public TaskResponse create(TaskRequest r){Project p=projects.findById(r.projectId()).orElseThrow(()->new ResourceNotFoundException("Project not found"));Task t=new Task();t.setTitle(r.title().trim());t.setDescription(r.description());t.setPriority(r.priority()==null?Priority.MEDIUM:r.priority());t.setStatus(r.status()==null?TaskStatus.TODO:r.status());t.setDeadline(r.deadline());t.setProject(p);t.setAssignedTo(assignee(r.assignedToId()));return view(tasks.save(t));}
    public TaskResponse update(Long id,TaskRequest r){Task t=tasks.findById(id).orElseThrow(()->new ResourceNotFoundException("Task not found"));Project p=projects.findById(r.projectId()).orElseThrow(()->new ResourceNotFoundException("Project not found"));t.setTitle(r.title().trim());t.setDescription(r.description());t.setPriority(r.priority()==null?Priority.MEDIUM:r.priority());t.setStatus(r.status()==null?TaskStatus.TODO:r.status());t.setDeadline(r.deadline());t.setProject(p);t.setAssignedTo(assignee(r.assignedToId()));return view(tasks.save(t));}
    public TaskResponse updateStatus(Long id,TaskStatus status,User actor){Task t=tasks.findById(id).orElseThrow(()->new ResourceNotFoundException("Task not found"));if(actor.getRole()!=Role.ADMIN&&(t.getAssignedTo()==null||!t.getAssignedTo().getId().equals(actor.getId())))throw new org.springframework.security.access.AccessDeniedException("You can only update your assigned tasks");t.setStatus(status);return view(tasks.save(t));}
    public void delete(Long id){tasks.delete(tasks.findById(id).orElseThrow(()->new ResourceNotFoundException("Task not found")));}
}
