package org.tim.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tim.entities.*;
import org.tim.repositories.*;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static io.github.benas.randombeans.api.EnhancedRandom.random;

@ExtendWith({SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class SpringTestsCustomExtension {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private TranslationAgencyRepository translationAgencyRepository;

    @Autowired
    private TranslationRepository translationRepository;

    @Autowired
    private LocaleWrapperRepository localeWrapperRepository;

    @Autowired
    private TranslationVersionRepository translationVersionRepository;

    @Autowired
	private MessageVersionRepository messageVersionRepository;

    @BeforeEach
    public void prepareDatabaseForTests() {
        clear();
    }

    public void clear() {
        translationVersionRepository.deleteAll();
		messageVersionRepository.deleteAll();
        translationRepository.deleteAll();
        messageRepository.deleteAll();
        translationAgencyRepository.deleteAll();
        projectRepository.deleteAll();
        localeWrapperRepository.deleteAll();
    }

    public Project createEmptyGermanToEnglishAndFrenchProject(){
        Project project = random(Project.class);
        project.setSourceLocale(Locale.GERMAN);
        LocaleWrapper localeWrapperEN = new LocaleWrapper(Locale.ENGLISH);
        LocaleWrapper localeWrapperFR = new LocaleWrapper(Locale.FRENCH);
        project.addTargetLocale(Arrays.asList(localeWrapperEN, localeWrapperFR));
        return projectRepository.save(project);
    }

    public Project createEmptyGermanToEnglishProject(){
        Project project = random(Project.class);
        project.setSourceLocale(Locale.GERMAN);
        LocaleWrapper localeWrapperEN = new LocaleWrapper(Locale.ENGLISH);
        project.addTargetLocale(Arrays.asList(localeWrapperEN));
        return projectRepository.save(project);
    }

    public Project createEmptyGermanToEnglishProjectWithSubstituteLocales(){
        LocaleWrapper localeWrapperEN = localeWrapperRepository.save(new LocaleWrapper(Locale.ENGLISH));
        LocaleWrapper localeWrapperUK = localeWrapperRepository.save(new LocaleWrapper(Locale.UK));
        Project project = random(Project.class);
        project.setSourceLocale(Locale.GERMAN);
        project.addTargetLocale(Arrays.asList(localeWrapperEN));
        project.getReplaceableLocaleToItsSubstitute().put(localeWrapperEN, localeWrapperUK);
        return projectRepository.save(project);
    }
    public Project createEmptyGermanToEnglishProjectWithTwoSubstituteLocales() {
        LocaleWrapper localeWrapperEN = localeWrapperRepository.save(new LocaleWrapper(Locale.ENGLISH));
        LocaleWrapper localeWrapperUS = localeWrapperRepository.save(new LocaleWrapper(Locale.US));
        LocaleWrapper localeWrapperUK = localeWrapperRepository.save(new LocaleWrapper(Locale.UK));
        Project project = random(Project.class);
        project.setSourceLocale(Locale.GERMAN);
        project.addTargetLocale(Arrays.asList(localeWrapperEN));
        project.addTargetLocale(Arrays.asList(localeWrapperUS));
        project.addTargetLocale(Arrays.asList(localeWrapperUK));
        project.getReplaceableLocaleToItsSubstitute().put(localeWrapperEN, localeWrapperUS);
        project.getReplaceableLocaleToItsSubstitute().put(localeWrapperUS, localeWrapperUK);
        return projectRepository.save(project);
    }

    public Message createRandomMessage(Project project){
        Message message = random(Message.class);
        message.setProject(project);
        return messageRepository.save(message);
    }

    public void createTenRandomMessages(Project project) {
        for(int i = 0; i < 10; i++){
            Message message = random(Message.class);
            message.setProject(project);
            messageRepository.save(message);
        }
    }

    public void createTranslationsForMessages(){
        List<Message> messages = messageRepository.findAll();
        for(Message message : messages){
            Translation translation = new Translation();
            translation.setIsValid(true);
            translation.setLocale(Locale.ENGLISH);
            translation.setContent("content");
            translation.setMessage(message);
            translationRepository.save(translation);
        }
    }

    public int getMessagesCount(){
       return messageRepository.findAll().size();
    }
}
