package org.tim.databaseSeed;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.LocaleUtils;
import org.springframework.stereotype.Service;
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

    public Map<String, Project> initProjects() {

        Map<String, Project> projects = new HashMap<>();

        Project projectP1 = new Project("Ocado - supermarket website", new Locale("en_GB"));
        projectP1.addTargetLocale(Arrays.asList(createLocale("en_US"), createLocale("pl_PL"), createLocale("de_DE"),
                createLocale("ar_LY"), createLocale("ko_KR")));
        projectP1 = projectRepository.save(projectP1);
        projectP1.updateSubstituteLocale(createLocale("ar_LY"), createLocale("pl_PL"));
        projectP1.updateSubstituteLocale(createLocale("pl_PL"), createLocale("ko_KR"));
        projects.put("projectP1", projectRepository.save(projectP1));

        Project projectP2 = new Project("Ocado - strona prezentująca przedsiębiorstwo", new Locale("pl_PL"));
        projectP2.addTargetLocale(Arrays.asList(createLocale("en_US"), createLocale("en_GB"), createLocale("de_DE"), createLocale("ko_KR"), createLocale("ar_LY")));
        projects.put("projectP2", projectRepository.save(projectP2));

        Project projectP3 = new Project("Project with a lot of locales", new Locale("en_US"));
        projectP3.addTargetLocale(Arrays.asList(
        		createLocale("pl_PL"), createLocale("en_GB"), createLocale("af_NA"), createLocale("af_ZA"),
				createLocale("ak_GH"), createLocale("sq_AL"), createLocale("hy_AM"), createLocale("ar_DZ"),
				createLocale("ar_SD"), createLocale("de_DE"), createLocale("ar_EG"), createLocale("bn_IN"),
				createLocale("bs_BA"), createLocale("bg_BG"), createLocale("my_MM"), createLocale("ca_ES"),
				createLocale("kw_GB"), createLocale("da_DK"), createLocale("en_AS"), createLocale("en_AU"),
				createLocale("en_VI"), createLocale("en_ZA"), createLocale("fr_MG"), createLocale("fr_MC"),
				createLocale("fr_BL"), createLocale("ko_KR"), createLocale("ar_LY"), createLocale("gu_IN")));
        projects.put("projectP3", projectRepository.save(projectP3));

        return projects;
    }

	private Locale createLocale(String name) {
		return LocaleUtils.toLocale(name);
	}
}
