package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.ResponseWrapper;
import com.cydeo.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
public class ProjectController{

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> getProjects(){
        List<ProjectDTO> projects = projectService.listAllProjects();

        return ResponseEntity.ok(new ResponseWrapper(true, "Projects successfully retrieved", HttpStatus.OK.value(), projects));

    }

    @GetMapping("{projectCode}")
    public ResponseEntity<ResponseWrapper> getProjectByCode(@PathVariable ("projectCode") String projectCode){
        ProjectDTO project = projectService.getByProjectCode(projectCode);
        return ResponseEntity.ok(new ResponseWrapper(true, "Project successfully retrieved", HttpStatus.OK.value(), project));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createProject(@RequestBody ProjectDTO projectDTO){
        projectService.save(projectDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("Project created", HttpStatus.CREATED));
    }

    @PutMapping
    public ResponseEntity<ResponseWrapper> updateProject(@RequestBody ProjectDTO projectDTO){
        projectService.update(projectDTO);

        return ResponseEntity.ok(new ResponseWrapper("Project created", HttpStatus.CREATED));
    }





}
