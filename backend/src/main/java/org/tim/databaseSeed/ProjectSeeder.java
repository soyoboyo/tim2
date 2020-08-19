package org.tim.databaseSeed;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tim.entities.LocaleWrapper;
import org.tim.entities.Project;
import org.tim.repositories.ProjectRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectSeeder {

	private final ProjectRepository projectRepository;

	@SuppressWarnings("unchecked")
	public Map<String, Project> initProjects(Map<String, LocaleWrapper> locales) {
		Map<String, Project> projects = new HashMap<>();
		ArrayList<LinkedHashMap<String, Object>> projectsArray = SeederUtils.getObjectsFromJSON("project-seed.json");

		for (LinkedHashMap<String, Object> p : projectsArray) {
			String name = (String) p.get("name");
			String sourceLocale = (String) p.get("sourceLocale");
			Project project = new Project(name, new Locale(sourceLocale));
			project.addTargetLocale(getWrappedLocales(locales, (ArrayList<String>) p.get("targetLocales")));
			project = projectRepository.save(project);
			updateSubstituteLocales(locales, (LinkedHashMap<String, Object>) p.get("substituteLocales"), project);
			projects.put((String) p.get("uuid"), projectRepository.save(project));
		}
		return projects;
	}

	private static List<LocaleWrapper> getWrappedLocales(Map<String, LocaleWrapper> locales, ArrayList<String> stringLocales) {
		return stringLocales.stream()
				.map(locales::get)
				.collect(Collectors.toList());
	}

	private static void updateSubstituteLocales(Map<String, LocaleWrapper> locales, LinkedHashMap<String, Object> stringLocales, Project project) {
		stringLocales.forEach((key, value) -> project.updateSubstituteLocale(
				locales.get(key),
				locales.get(value)
		));
	}
}
