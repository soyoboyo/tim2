package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.LocaleUtils;
import org.springframework.stereotype.Service;
import org.tim.DTOs.input.NewProjectRequest;
import org.tim.DTOs.output.ProjectForDeveloper;
import org.tim.configurations.Done;
import org.tim.configurations.ToDo;
import org.tim.entities.Project;
import org.tim.exceptions.EntityNotFoundException;
import org.tim.exceptions.ValidationException;
import org.tim.repositories.ProjectRepository;
import org.tim.translators.LocaleTranslator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.tim.utils.UserMessages.LCL_NOT_FOUND;
import static org.tim.utils.UserMessages.formatMessage;

@Service
@RequiredArgsConstructor
public class ProjectService {

	private final ProjectRepository projectRepository;
	private final LocaleTranslator localeTranslator;

	@Done
	public List<Project> getAllProjects() {
		return StreamSupport.
				stream(projectRepository.findAll().spliterator(), false)
				.collect(Collectors.toList());
	}

	@Done
	public Project createProject(NewProjectRequest projectRequest) throws IllegalArgumentException {
		validateProjectNameUniqueness(projectRequest.getName(), Optional.empty());

		Project project = new Project();
		mapProjectRequestToProject(projectRequest, project);

		return projectRepository.save(project);
	}

	@Done
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

	@Done
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

	@Done
	private void validateProjectNameUniqueness(String projectName, Optional<String> actualName) {
		Optional<Project> searchedProject = projectRepository.findByName(projectName);
		if (searchedProject.isPresent() && (
				actualName.isEmpty() || !actualName.get().equals(searchedProject.get().getName()))) {
			throw new ValidationException("Project name must be unique. Please choose other name.");
		}
	}

	public List<ProjectForDeveloper> getAllProjectsForDeveloper() {
		List<Project> originalProjects = StreamSupport
				.stream(projectRepository.findAll().spliterator(), false)
				.collect(Collectors.toList());
		List<ProjectForDeveloper> projects = new ArrayList<>(originalProjects.size());
		for (Project p : originalProjects) {
			ProjectForDeveloper projectForDeveloper = new ProjectForDeveloper();
			projectForDeveloper.setId(p.getId());
			projectForDeveloper.setName(p.getName());
			String[] sources = p.getSourceLocale().toString().split("_");
			projectForDeveloper.setSourceLanguage(sources[0]);
			projectForDeveloper.setSourceCountry(sources[1]);

			TreeSet<String> newTargetLocales = new TreeSet<>();
			for (Locale lw : p.getTargetLocales()) {
				String targetLocale = lw.toString();
				newTargetLocales.add(targetLocale);
			}
			// TODO: sort target locales alphabetically
			projectForDeveloper.setTargetLocales(newTargetLocales);

			TreeSet<String> availableReplacements = new TreeSet<>(newTargetLocales);
			availableReplacements.add(p.getSourceLocale().toString());
			projectForDeveloper.setAvailableReplacements(availableReplacements);

			HashMap<String, String> substitutes = new HashMap<>(p.getReplaceableLocaleToItsSubstitute().size());

			for (Map.Entry<Locale, Locale> entry : p.getReplaceableLocaleToItsSubstitute().entrySet()) {
				String replaced = entry.getKey().toString().split("=")[2].substring(0, 6 - 1);
				String replacement = entry.getValue().toString().split("=")[2].substring(0, 6 - 1);

				substitutes.put(replaced, replacement);
			}
			projectForDeveloper.setSubstitutes(substitutes);

			projects.add(projectForDeveloper);
		}

		return projects;
	}

}
