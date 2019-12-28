package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.LocaleUtils;
import org.springframework.stereotype.Service;
import org.tim.DTOs.input.NewProjectRequest;
import org.tim.DTOs.output.ProjectForDeveloper;
import org.tim.configurations.Done;
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

	public List<Project> getAllProjects() {
		Iterable<Project> p = projectRepository.findAll();
		return StreamSupport.stream(p.spliterator(), false).collect(Collectors.toList());
	}

	@Done
	public Project createProject(NewProjectRequest projectRequest) throws IllegalArgumentException {
		validateProjectNameUniqueness(projectRequest.getName());

		Project project = new Project();
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

		return projectRepository.save(project);
	}

	@Done
	private void validateProjectNameUniqueness(String projectName) {
		if (projectRepository.findByName(projectName).isPresent()) {
			throw new ValidationException("Project name must be unique. Please choose other name.");
		}
	}

	private Project mapProjectDTOIntoProject(Project project, NewProjectRequest projectRequest) {
		project.setName(projectRequest.getName());
		project.setSourceLocale(LocaleUtils.toLocale(projectRequest.getSourceLocale()));
		List<Locale> locales = new LinkedList<>();
		projectRequest.getTargetLocales().forEach(locale -> {
			locales.add(LocaleUtils.toLocale(locale));
		});
//		persistentLocaleWrappers.forEach(localeWrapper -> {
//			persistentLocaleWrappersMap.put(localeWrapper.getLocale(), localeWrapper);
//		});
//		projectDTO.getTargetLocales().forEach(locale -> {
//			Locale localeToSave = LocaleUtils.toLocale(locale);
//			if (persistentLocaleWrappersMap.get(localeToSave) == null) {
//				localeWrappersToSave.add(new LocaleWrapper(localeToSave));
//			}
//		});
		//project.addTargetLocale(StreamSupport.stream(p.spliterator(), false).collect(Collectors.toList()));
		Map<Locale, Locale> targetLocales = new HashMap<>();
		project.getTargetLocales().forEach(targetLocal -> {
			targetLocales.put(targetLocal, targetLocal);
		});
		projectRequest.getReplaceableLocaleToItsSubstitute().forEach((locale1, locale2) -> {
			project.updateSubstituteLocale(getLocaleWrapperByLocale(locale1, targetLocales),
					getLocaleWrapperByLocale(locale2, targetLocales));
		});
		return project;
	}

	public Project updateProject(NewProjectRequest projectRequest, String id) {
		Project project = projectRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("project"));
		//validateLocales(projectRequest);
		project.getReplaceableLocaleToItsSubstitute().clear();
		project.getTargetLocales().clear();
		if (!projectRepository.existsByName(projectRequest.getName()) || (project.getName().equals(projectRequest.getName()))) {
			project = projectRepository.save(mapProjectDTOIntoProject(project, projectRequest));
		} else {
			throw new ValidationException("Project name must be unique");
		}
		return project;
	}


	//TODO Probably to remove
//	private void validateLocales(NewProjectRequest projectRequest) {
//		String msg = "";
//		boolean isSourceLocaleValid = true;
//		try {
//			LocaleUtils.toLocale(projectRequest.getSourceLocale());
//		} catch (IllegalArgumentException e) {
//			msg += "Source locale: " + projectRequest.getSourceLocale() + " was given in the wrong format. \n";
//			isSourceLocaleValid = false;
//		}
//		String msg2 = "Target locales: ";
//		boolean areTargetLocalesValid = true;
//		for (String locale : projectRequest.getTargetLocales()) {
//			try {
//				LocaleUtils.toLocale(locale);
//			} catch (IllegalArgumentException e) {
//				msg2 += locale + " ";
//				areTargetLocalesValid = false;
//			}
//		}
//		if (!areTargetLocalesValid) {
//			msg += msg2 + "was given in the wrong format. \n";
//		}
//		String msg3 = "Replaceable locale to it's substitute as: ";
//		boolean areReplaceableLocalesValid = true;
//		for (Map.Entry<String, String> entry : projectRequest.getReplaceableLocaleToItsSubstitute().entrySet()) {
//			try {
//				LocaleUtils.toLocale(entry.getKey());
//				LocaleUtils.toLocale(entry.getValue());
//			} catch (IllegalArgumentException e) {
//				msg3 += entry.getKey() + "->" + entry.getValue() + " ";
//				areReplaceableLocalesValid = false;
//			}
//		}
//		if (!areReplaceableLocalesValid) {
//			msg += msg3 + "was given in the wrong format. \n";
//		}
//		if (!isSourceLocaleValid || !areReplaceableLocalesValid || !areTargetLocalesValid) {
//			throw new ValidationException(msg);
//		}
//	}

	private Locale getLocaleWrapperByLocale(String localeAsString, Map<Locale, Locale> targetLocales) {
		Locale localeWrapper = targetLocales.get(LocaleUtils.toLocale(localeAsString));
		if (localeWrapper != null) {
			return localeWrapper;
		}
		throw new ValidationException(formatMessage(LCL_NOT_FOUND, localeAsString));
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
