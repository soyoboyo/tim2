package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tim.DTOs.input.NewProjectRequest;
import org.tim.DTOs.output.ProjectForDeveloperResponse;
import org.tim.annotations.Done;
import org.tim.annotations.ToDo;
import org.tim.entities.Project;
import org.tim.exceptions.EntityNotFoundException;
import org.tim.exceptions.ValidationException;
import org.tim.repositories.ProjectRepository;
import org.tim.translators.LocaleTranslator;
import org.tim.utils.Pages;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ProjectService {

	private final ProjectRepository projectRepository;
	private final LocaleTranslator localeTranslator;

	@Done
	public List<Project> getAllProjects() {
		return projectRepository.findAll(Pages.all()).getContent();
	}

	@Done
	public Project createProject(NewProjectRequest projectRequest) throws IllegalArgumentException {
		validateProjectNameUniqueness(projectRequest.getName(), Optional.empty());

		Project project = new Project();
		mapProjectRequestToProject(projectRequest, project);

		return projectRepository.save(project);
	}

	@ToDo("Has developer access to project??," +
			"What should happen to the translations when locale will be removed??")
	public Project updateProject(NewProjectRequest projectRequest, String id) {
		Project project = projectRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("project"));

		validateProjectNameUniqueness(projectRequest.getName(), Optional.of(project.getName()));

		project.getTargetLocales().clear();
		project.getReplaceableLocaleToItsSubstitute().clear();
		mapProjectRequestToProject(projectRequest, project);

		return projectRepository.save(project);
	}

	private void mapProjectRequestToProject(NewProjectRequest projectRequest, Project project) {
		project.setName(projectRequest.getName());
		project.setSourceLocale(localeTranslator.execute(projectRequest.getSourceLocale()));
		project.addTargetLocale(
				projectRequest.getTargetLocales()
						.parallelStream()
						.map(locale -> localeTranslator.execute(locale))
						.collect(Collectors.toList()));
		for (Map.Entry<String, String> r : projectRequest.getReplaceableLocaleToItsSubstitute().entrySet()) {
			project.updateSubstituteLocale(
					localeTranslator.execute(r.getKey()),
					localeTranslator.execute(r.getValue()));
		}
	}

	private void validateProjectNameUniqueness(String projectName, Optional<String> actualName) {
		Optional<Project> searchedProject = projectRepository.findByName(projectName);
		if (searchedProject.isPresent() && (
				actualName.isEmpty() || !actualName.get().equals(searchedProject.get().getName()))) {
			throw new ValidationException("Project name must be unique. Please choose other name.");
		}
	}

	public List<ProjectForDeveloperResponse> getAllProjectsForDeveloper() {
		;

		return projectRepository.findAll(Pages.all()).getContent()
				.stream()
				.map(project -> mapProjectForDeveloper(project))
				.collect(Collectors.toList());
	}

	private ProjectForDeveloperResponse mapProjectForDeveloper(Project project) {
		ProjectForDeveloperResponse projectForDeveloper = new ProjectForDeveloperResponse();

		projectForDeveloper.setId(project.getId());
		projectForDeveloper.setName(project.getName());
		projectForDeveloper.setSourceLanguage(project.getSourceLocale().getLanguage());
		projectForDeveloper.setSourceCountry(project.getSourceLocale().getCountry());

		Set<String> newTargetLocales = project.getTargetLocales()
				.stream()
				.map(l -> l.toString())
				.collect(Collectors.toSet());
		projectForDeveloper.setTargetLocales(newTargetLocales);
		Set<String> availableReplacements = new TreeSet<>(newTargetLocales);
		availableReplacements.add(project.getSourceLocale().toString());
		projectForDeveloper.setAvailableReplacements(availableReplacements);

		Map<String, String> substitutes = new HashMap<>(project.getReplaceableLocaleToItsSubstitute().size());
		for (Map.Entry<Locale, Locale> entry : project.getReplaceableLocaleToItsSubstitute().entrySet()) {
			String replaced = entry.getKey().toString();
			String replacement = entry.getValue().toString();
			substitutes.put(replaced, replacement);
		}
		projectForDeveloper.setSubstitutes(substitutes);

		return projectForDeveloper;
	}

}
