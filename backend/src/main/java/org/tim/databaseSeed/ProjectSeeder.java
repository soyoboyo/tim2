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

        return projects;
    }
}
