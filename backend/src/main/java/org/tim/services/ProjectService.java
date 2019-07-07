package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.LocaleUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tim.DTOs.input.ProjectDTO;
import org.tim.DTOs.output.ProjectForDeveloper;
import org.tim.entities.LocaleWrapper;
import org.tim.entities.Project;
import org.tim.exceptions.EntityNotFoundException;
import org.tim.exceptions.ValidationException;
import org.tim.repositories.LocaleWrapperRepository;
import org.tim.repositories.ProjectRepository;

import java.util.*;

import static org.tim.utils.UserMessages.LCL_NOT_FOUND;
import static org.tim.utils.UserMessages.formatMessage;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

	private final ProjectRepository projectRepository;
	private final LocaleWrapperRepository localeWrapperRepository;

	public List<Project> getAllProjects() {
		return projectRepository.findAll();
	}

	public Project createProject(ProjectDTO projectDTO) throws IllegalArgumentException {
		Project project = new Project();
		validateLocales(projectDTO);
		project.setSourceLocale(LocaleUtils.toLocale(projectDTO.getSourceLocale()));
		if (!projectRepository.existsByName(projectDTO.getName())) {
			project = projectRepository.save(mapProjectDTOIntoProject(project, projectDTO));
		} else {
			throw new ValidationException("Project name must be unique");
		}
		return project;
	}

	public Project updateProject(ProjectDTO projectDTO, Long id) {
		Project project = projectRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("project"));
		validateLocales(projectDTO);
		project.getReplaceableLocaleToItsSubstitute().clear();
		project.getTargetLocales().clear();
		if (!projectRepository.existsByName(projectDTO.getName()) || (project.getName().equals(projectDTO.getName()))) {
			project = projectRepository.save(mapProjectDTOIntoProject(project, projectDTO));
		} else {
			throw new ValidationException("Project name must be unique");
		}
		return project;
	}

	private Project mapProjectDTOIntoProject(Project project, ProjectDTO projectDTO) {
		project.setName(projectDTO.getName());
		project.setSourceLocale(LocaleUtils.toLocale(projectDTO.getSourceLocale()));
		List<Locale> locales = new LinkedList<>();
		projectDTO.getTargetLocales().forEach(locale -> {
			locales.add(LocaleUtils.toLocale(locale));
		});
		List<LocaleWrapper> persistentLocaleWrappers = localeWrapperRepository.findAllByLocaleIn(locales);
		List<LocaleWrapper> localeWrappersToSave = new LinkedList<>();
		Map<Locale, LocaleWrapper> persistentLocaleWrappersMap = new HashMap<>();
		persistentLocaleWrappers.forEach(localeWrapper -> {
			persistentLocaleWrappersMap.put(localeWrapper.getLocale(), localeWrapper);
		});
		projectDTO.getTargetLocales().forEach(locale -> {
			Locale localeToSave = LocaleUtils.toLocale(locale);
			if (persistentLocaleWrappersMap.get(localeToSave) == null) {
				localeWrappersToSave.add(new LocaleWrapper(localeToSave));
			}
		});
		project.addTargetLocale(localeWrapperRepository.saveAll(localeWrappersToSave));
		project.addTargetLocale(persistentLocaleWrappers);
		Map<Locale, LocaleWrapper> targetLocales = new HashMap<>();
		project.getTargetLocales().forEach(targetLocal -> {
			targetLocales.put(targetLocal.getLocale(), targetLocal);
		});
		projectDTO.getReplaceableLocaleToItsSubstitute().forEach((locale1, locale2) -> {
			project.updateSubstituteLocale(getLocaleWrapperByLocale(locale1, targetLocales),
					getLocaleWrapperByLocale(locale2, targetLocales));
		});
		return project;
	}

	private void validateLocales(ProjectDTO projectDTO) {
		String msg = "";
		boolean isSourceLocaleValid = true;
		try {
			LocaleUtils.toLocale(projectDTO.getSourceLocale());
		} catch (IllegalArgumentException e) {
			msg += "Source locale: " + projectDTO.getSourceLocale() + " was given in the wrong format. \n";
			isSourceLocaleValid = false;
		}
		String msg2 = "Target locales: ";
		boolean areTargetLocalesValid = true;
		for (String locale : projectDTO.getTargetLocales()) {
			try {
				LocaleUtils.toLocale(locale);
			} catch (IllegalArgumentException e) {
				msg2 += locale + " ";
				areTargetLocalesValid = false;
			}
		}
		if (!areTargetLocalesValid) {
			msg += msg2 + "was given in the wrong format. \n";
		}
		String msg3 = "Replaceable locale to it's substitute as: ";
		boolean areReplaceableLocalesValid = true;
		for (Map.Entry<String, String> entry : projectDTO.getReplaceableLocaleToItsSubstitute().entrySet()) {
			try {
				LocaleUtils.toLocale(entry.getKey());
				LocaleUtils.toLocale(entry.getValue());
			} catch (IllegalArgumentException e) {
				msg3 += entry.getKey() + "->" + entry.getValue() + " ";
				areReplaceableLocalesValid = false;
			}
		}
		if (!areReplaceableLocalesValid) {
			msg += msg3 + "was given in the wrong format. \n";
		}
		if (!isSourceLocaleValid || !areReplaceableLocalesValid || !areTargetLocalesValid) {
			throw new ValidationException(msg);
		}
	}

	private LocaleWrapper getLocaleWrapperByLocale(String localeAsString, Map<Locale, LocaleWrapper> targetLocales) {
		LocaleWrapper localeWrapper = targetLocales.get(LocaleUtils.toLocale(localeAsString));
		if (localeWrapper != null) {
			return localeWrapper;
		}
		throw new ValidationException(formatMessage(LCL_NOT_FOUND, localeAsString));
	}

	public List<ProjectForDeveloper> getAllProjectsForDeveloper() {
		List<Project> originalProjects = projectRepository.findAll();
		List<ProjectForDeveloper> projects = new ArrayList<>(originalProjects.size());
		for (Project p : originalProjects) {
			ProjectForDeveloper projectForDeveloper = new ProjectForDeveloper();
			projectForDeveloper.setId(p.getId());
			projectForDeveloper.setName(p.getName());
			String[] sources = p.getSourceLocale().toString().split("_");
			projectForDeveloper.setSourceLanguage(sources[0]);
			projectForDeveloper.setSourceCountry(sources[1]);

			TreeSet<String> newTargetLocales = new TreeSet<>();
			for (LocaleWrapper lw : p.getTargetLocales()) {
				String targetLocale = lw.getLocale().toString();
				newTargetLocales.add(targetLocale);
			}
			projectForDeveloper.setTargetLocales(newTargetLocales);

			TreeSet<String> availableReplacements = new TreeSet<>(newTargetLocales);
			availableReplacements.add(p.getSourceLocale().toString());
			projectForDeveloper.setAvailableReplacements(availableReplacements);

			HashMap<String, String> substitutes = new HashMap<>(p.getReplaceableLocaleToItsSubstitute().size());

			for (Map.Entry<LocaleWrapper, LocaleWrapper> entry : p.getReplaceableLocaleToItsSubstitute().entrySet()){
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
