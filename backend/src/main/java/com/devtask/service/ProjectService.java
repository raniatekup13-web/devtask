package com.devtask.service;

import com.devtask.exception.ResourceNotFoundException;
import com.devtask.dto.ProjectRequest;
import com.devtask.model.Project;
import com.devtask.model.User;
import com.devtask.dto.ProjectResponse;
import com.devtask.repository.ProjectRepository;
import com.devtask.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ProjectService {
    private final ProjectRepository repo; private final TaskRepository tasks;
    public ProjectService(ProjectRepository repo, TaskRepository tasks){this.repo=repo;this.tasks=tasks;}
    private ProjectResponse view(Project p){return new ProjectResponse(p.getId(),p.getName(),p.getDescription(),p.getOwner()==null?null:p.getOwner().getEmail(),tasks.countByProjectId(p.getId()));}
    public List<ProjectResponse> all(){return repo.findAll().stream().map(this::view).toList();}
    public ProjectResponse get(Long id){return view(repo.findById(id).orElseThrow(()->new ResourceNotFoundException("Project not found")));}
    public ProjectResponse create(ProjectRequest r, User owner){Project p=new Project();p.setName(r.name().trim());p.setDescription(r.description());p.setOwner(owner);return view(repo.save(p));}
    public ProjectResponse update(Long id,ProjectRequest r){Project p=repo.findById(id).orElseThrow(()->new ResourceNotFoundException("Project not found"));p.setName(r.name().trim());p.setDescription(r.description());return view(repo.save(p));}
    public void delete(Long id){repo.delete(repo.findById(id).orElseThrow(()->new ResourceNotFoundException("Project not found")));}
}
