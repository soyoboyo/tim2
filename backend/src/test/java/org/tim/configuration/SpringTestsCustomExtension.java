package org.tim.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tim.entities.Message;
import org.tim.entities.Project;
import org.tim.entities.Translation;
import org.tim.repositories.*;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static io.github.benas.randombeans.api.EnhancedRandom.random;

@ActiveProfiles("tests")
@ExtendWith({SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class SpringTestsCustomExtension {

    @Autowired
    protected ProjectRepository projectRepository;

    @Autowired
    protected MessageRepository messageRepository;

    @Autowired
    protected TranslationAgencyRepository translationAgencyRepository;

    @Autowired
    protected TranslationRepository translationRepository;

    @Autowired
    protected TranslationVersionRepository translationVersionRepository;

    @Autowired
    protected MessageVersionRepository messageVersionRepository;

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
    }

    public Project createEmptyGermanToEnglishAndFrenchProject(){
        Project project = random(Project.class);
        project.setSourceLocale(Locale.GERMAN);
//        LocaleWrapper localeWrapperEN = new LocaleWrapper(Locale.ENGLISH);
//        LocaleWrapper localeWrapperFR = new LocaleWrapper(Locale.FRENCH);
//        project.addTargetLocale(Arrays.asList(localeWrapperEN, localeWrapperFR));
        return projectRepository.save(project);
    }

    public Project createEmptyGermanToEnglishProject(){
        Project project = random(Project.class);
        project.setSourceLocale(Locale.GERMAN);
//        LocaleWrapper localeWrapperEN = new LocaleWrapper(Locale.ENGLISH);
//        project.addTargetLocale(Arrays.asList(localeWrapperEN));
        return projectRepository.save(project);
    }

    public Project createEmptyGermanToEnglishProjectWithSubstituteLocales(){
//        LocaleWrapper localeWrapperEN = localeWrapperRepository.save(new LocaleWrapper(Locale.ENGLISH));
//        LocaleWrapper localeWrapperUK = localeWrapperRepository.save(new LocaleWrapper(Locale.UK));
        Project project = random(Project.class);
        project.setSourceLocale(Locale.GERMAN);
//        project.addTargetLocale(Arrays.asList(localeWrapperEN));
//        project.getReplaceableLocaleToItsSubstitute().put(localeWrapperEN, localeWrapperUK);
        return projectRepository.save(project);
    }
    public Project createEmptyGermanToEnglishProjectWithTwoSubstituteLocales() {
//        LocaleWrapper localeWrapperEN = localeWrapperRepository.save(new LocaleWrapper(Locale.ENGLISH));
//        LocaleWrapper localeWrapperUS = localeWrapperRepository.save(new LocaleWrapper(Locale.US));
//        LocaleWrapper localeWrapperUK = localeWrapperRepository.save(new LocaleWrapper(Locale.UK));
        Project project = random(Project.class);
        project.setSourceLocale(Locale.GERMAN);
//        project.addTargetLocale(Arrays.asList(localeWrapperEN));
//        project.addTargetLocale(Arrays.asList(localeWrapperUS));
//        project.addTargetLocale(Arrays.asList(localeWrapperUK));
//        project.getReplaceableLocaleToItsSubstitute().put(localeWrapperEN, localeWrapperUS);
//        project.getReplaceableLocaleToItsSubstitute().put(localeWrapperUS, localeWrapperUK);
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
        List<Message> messages = StreamSupport
                .stream(messageRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
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
       return StreamSupport
               .stream(messageRepository.findAll().spliterator(), false)
               .collect(Collectors.toList()).size();
    }
}
