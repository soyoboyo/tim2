package org.tim.databaseSeed;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tim.entities.LocaleWrapper;
import org.tim.entities.Message;
import org.tim.entities.Project;
import javax.annotation.PostConstruct;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class MainSeeder {

    private final LocaleWrapperSeeder localeWrapperSeeder;
    private final TranslationAgencySeeder translationAgencySeeder;
    private final ProjectSeeder projectSeeder;
    private final MessageSeeder messageSeeder;
    private final TranslationSeeder translationSeeder;

    @Transactional
    @PostConstruct
    public void init() {
        Map<String, LocaleWrapper> locales = localeWrapperSeeder.initLocales();
        translationAgencySeeder.initTransactionAgencies(locales);
        Map<String, Project> projects = projectSeeder.initProjects(locales);
        Map<String, Message> messages = messageSeeder.initMessages(projects);
        translationSeeder.initTranslations(messages);
    }
}
