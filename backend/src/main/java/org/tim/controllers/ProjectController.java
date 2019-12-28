package org.tim.controllers;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.tim.DTOs.input.NewProjectRequest;
import org.tim.DTOs.output.AggregatedInfoForDeveloper;
import org.tim.DTOs.output.ProjectForDeveloper;
import org.tim.configurations.Done;
import org.tim.entities.Project;
import org.tim.services.AggregatedInfoService;
import org.tim.services.ProjectService;
import org.tim.validators.RequestsValidator;

import javax.validation.Valid;
import java.util.List;

import static org.tim.utils.Mapping.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + PROJECT)
public class ProjectController {

	private final ProjectService projectService;
	private final AggregatedInfoService aggregatedInfoService;
	private final RequestsValidator requestsValidator;

	@Done
	@ApiOperation(
			value = "Create project",
			notes = "Create project with given body:\n" +
					"* name -> Project name\n" +
					"* sourceLocale -> Source locale as a String\n" +
					"* targetLocales -> All target locales for which project will be translated (need to contain sourceLocale)\n" +
					"* replaceableLocaleToItsSubstitute -> Map for replaceable locales that will inform that if translation to " +
					"substitute locale is unavailable then translation to replaceable locale can be used. (Because languages " +
					"with this locales are similar). All chosen locales need to be available in targetLocales. Cannot contain cycles.")
	@PostMapping(CREATE)
	//@PreAuthorize("hasRole('ROLE_DEVELOPER')")
	public Project createProject(@RequestBody @Valid NewProjectRequest projectRequest, BindingResult bindingResult) {
		requestsValidator.execute(bindingResult);
		return projectService.createProject(projectRequest);
	}

	@Done
	@ApiOperation(
			value = "Update project",
			notes = "Please using the same params as during creation")
	@PostMapping(UPDATE)
	//@PreAuthorize("hasRole('ROLE_DEVELOPER')")
	public Project updateProject(@RequestBody @Valid NewProjectRequest projectRequest, @PathVariable String id, BindingResult bindingResult) {
		RequestsValidator.validate(bindingResult);
		return projectService.updateProject(projectRequest, id);
	}

	@Done
	@ApiOperation("Get all available project")
	@GetMapping(GET_ALL)
	//@PreAuthorize("hasAnyRole('ROLE_TRANSLATOR', 'ROLE_DEVELOPER')")
	public List<Project> getAllProjects() {
		return projectService.getAllProjects();
	}

	@GetMapping(DEVELOPER + GET_ALL)
	//@PreAuthorize("hasRole('ROLE_DEVELOPER')")
	public List<ProjectForDeveloper> getAllProjectsForDeveloper() {
		return projectService.getAllProjectsForDeveloper();
	}

	@GetMapping(DEVELOPER + AGGREGATE)
	//@PreAuthorize("hasRole('ROLE_DEVELOPER')")
	public AggregatedInfoForDeveloper getAggregatedInfoAboutTranslationsInProject(@PathVariable String id) {
		return aggregatedInfoService.getAggregatedInfoForDeveloper(id);
	}

}
