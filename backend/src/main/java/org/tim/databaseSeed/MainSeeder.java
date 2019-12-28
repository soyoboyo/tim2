package org.tim.databaseSeed;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tim.entities.Message;
import org.tim.entities.Project;

import javax.annotation.PostConstruct;
import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class MainSeeder {

    private final TranslationAgencySeeder translationAgencySeeder;
    private final ProjectSeeder projectSeeder;
    private final MessageSeeder messageSeeder;
    private final TranslationSeeder translationSeeder;

    @Transactional
    @PostConstruct
    public void init() {
        translationAgencySeeder.initTransactionAgencies();
        Map<String, Project> projects = projectSeeder.initProjects();
        Map<String, Message> messages = messageSeeder.initMessages(projects);
        translationSeeder.initTranslations(messages);
    }
}
