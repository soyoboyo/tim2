package org.tim.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.tim.configuration.SpringTestsCustomExtension;
import org.tim.entities.LocaleWrapper;
import org.tim.entities.Message;
import org.tim.entities.Project;
import org.tim.entities.Translation;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ImportServiceTestIT extends SpringTestsCustomExtension {

    @Autowired
    private ImportService importService;

    private Project project;

    private MultipartFile importExampleDevFile = new MockMultipartFile("file.csv", "Test project name,\nkey,content\ntestKey1,testContent1\ntestKey2,testContent2".getBytes());
    private MultipartFile importExampleTranFile = new MockMultipartFile("file.csv", "Test project name,,\nen_US,,\nkey,content,translation\ntranKey1,Witaj,Hello\ntranKey2,Swiecie,World".getBytes());


    @BeforeEach
    void setUp() {
        project = createEmptyGermanToEnglishProject();
        project.setName("Test project name");
        LocaleWrapper localeWrapperEN = new LocaleWrapper(Locale.US);
        project.addTargetLocale(Arrays.asList(localeWrapperEN));
        projectRepository.save(project);

        Message messageOne = new Message("tranKey1", "Witaj", project);
        Message messageTwo = new Message("tranKey2", "Swiecie", project);

        messageRepository.saveAll(List.of(messageOne, messageTwo));
    }

    @Test
    void whenCorrectDeveloperFileImportedThenMessagesAreCreated() throws IOException {
        //given
        //when
        importService.importDeveloperCSVMessage(importExampleDevFile);
        //then
        assertEquals(4, messageRepository.findAll().size());
    }

    @Test
    @DisplayName("Create translations from file by translator")
    void whenCorrectTranslatorFileImportedThenTranslationsAreCreated() throws IOException {
        //given
        //when
        importService.importTranslatorCSVFile(importExampleTranFile);
        //then
        List<Translation> translations = translationRepository.findAll();

        assertAll(
                () -> assertEquals(2, translations.size()),
                () -> assertEquals("Hello", translations.get(0).getContent()),
                () -> assertEquals("World", translationRepository.findAll().get(1).getContent())
        );

    }
}