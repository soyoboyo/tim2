package org.tim.databaseSeed;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tim.entities.LocaleWrapper;
import org.tim.entities.Project;
import org.tim.repositories.ProjectRepository;

import java.util.*;
@Service
@RequiredArgsConstructor
public class ProjectSeeder {

	private final ProjectRepository projectRepository;

	public Map<String, Project> initProjects(Map<String, LocaleWrapper> locales) {

		Map<String, Project> projects = new HashMap<>();

		String path = "backend/src/main/resources/json-seed/project-seed.json";
		ArrayList<LinkedHashMap<String, Object>> projectsArray = SeederUtils.getObjectsFromJSON(path);


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
		List<LocaleWrapper> wrapped = new LinkedList<>();
		for (String locale : stringLocales) {
			wrapped.add(locales.get(locale));
		}
		return wrapped;
	}

	private static void updateSubstituteLocales(Map<String, LocaleWrapper> locales, LinkedHashMap<String, Object> stringLocales, Project project) {
		for (Map.Entry<String, Object> l : stringLocales.entrySet()) {
			String locale = l.getKey();
			String substitute = (String) l.getValue();
			project.updateSubstituteLocale(locales.get(locale), locales.get(substitute));
		}
	}
}
