package org.tim.databaseSeed;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tim.entities.LocaleWrapper;
import org.tim.entities.Project;
import org.tim.repositories.ProjectRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProjectSeeder {

	private final ProjectRepository projectRepository;

	public Map<String, Project> initProjects(Map<String, LocaleWrapper> locales) {

		Map<String, Project> projects = new HashMap<>();

		Project projectP1 = new Project("Ocado - supermarket website", new Locale("en_GB"));
		projectP1.addTargetLocale(Arrays.asList(locales.get("en_US"), locales.get("pl_PL"), locales.get("de_DE"),
				locales.get("ar_LY"), locales.get("ko_KR")));
		projectP1 = projectRepository.save(projectP1);
		projectP1.updateSubstituteLocale(locales.get("ar_LY"), locales.get("pl_PL"));
		projectP1.updateSubstituteLocale(locales.get("pl_PL"), locales.get("ko_KR"));
		projects.put("projectP1", projectRepository.save(projectP1));

		Project projectP2 = new Project("Ocado - strona prezentująca przedsiębiorstwo", new Locale("pl_PL"));
		projectP2.addTargetLocale(Arrays.asList(locales.get("en_US"), locales.get("en_GB"), locales.get("de_DE"), locales.get("ko_KR"), locales.get("ar_LY")));
		projects.put("projectP2", projectRepository.save(projectP2));

		Project projectP3 = new Project("Project with a lot of locales", new Locale("en_US"));
		projectP3.addTargetLocale(Arrays.asList(
				locales.get("pl_PL"), locales.get("en_GB"), locales.get("af_NA"), locales.get("af_ZA"),
				locales.get("ak_GH"), locales.get("sq_AL"), locales.get("hy_AM"), locales.get("ar_DZ"),
				locales.get("ar_SD"), locales.get("de_DE"), locales.get("ar_EG"), locales.get("bn_IN"),
				locales.get("bs_BA"), locales.get("bg_BG"), locales.get("my_MM"), locales.get("ca_ES"),
				locales.get("kw_GB"), locales.get("da_DK"), locales.get("en_AS"), locales.get("en_AU"),
				locales.get("en_VI"), locales.get("en_ZA"), locales.get("fr_MG"), locales.get("fr_MC"),
				locales.get("fr_BL"), locales.get("ko_KR"), locales.get("ar_LY"), locales.get("gu_IN")));
		projects.put("projectP3", projectRepository.save(projectP3));

		Project projectP4 = new Project("Example Angular app", new Locale("en_GB"));
		projectP4.addTargetLocale(Arrays.asList(locales.get("pl_PL")));
		projects.put("projectP4", projectRepository.save(projectP4));

		return projects;
	}
}
