package org.tim.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.tim.DTOs.input.ProjectDTO;
import org.tim.entities.Project;
import org.tim.services.ProjectService;
import org.tim.validators.DTOValidator;
import javax.validation.Valid;

import java.util.List;

import static org.tim.utils.Mapping.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_DEVELOPER')")
@RequestMapping(API_VERSION + PROJECT)
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping(GET_ALL)
	@PreAuthorize("hasRole('ROLE_TRANSLATOR')")
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @PostMapping(CREATE)
    public Project createProject(@RequestBody @Valid ProjectDTO projectDTO, BindingResult bindingResult) {
        DTOValidator.validate(bindingResult);
        return projectService.createProject(projectDTO);
    }

    @PostMapping(UPDATE)
    public Project updateProject(@RequestBody @Valid ProjectDTO projectDTO, @PathVariable Long id, BindingResult bindingResult) {
        DTOValidator.validate(bindingResult);
        return projectService.updateProject(projectDTO, id);
    }
}
