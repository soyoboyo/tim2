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
import org.tim.exceptions.EntityNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

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
    }

    @Test
    void whenCorrectDeveloperFileImportedThenMessagesAreCreated() throws Exception {
        //given
        //when
        importService.importDeveloperCSVMessage(importExampleDevFile);
        //then
        assertEquals(2, messageRepository.findAll().size());
    }

    @Test
    @DisplayName("Create translations from file by translator")
    void whenCorrectTranslatorFileImportedThenTranslationsAreCreated() throws Exception {
        //given
        Message messageOne = new Message("tranKey1", "Witaj", project);
        Message messageTwo = new Message("tranKey2", "Swiecie", project);

        messageRepository.saveAll(List.of(messageOne, messageTwo));
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

    @Test
    @DisplayName("When translation message key isn't found then rollback transaction and return message")
    void whenKeyIsNotFoundInDBFromTranslatorCSVFileThenRollback() throws Exception {
        //given
        saveMessagesToTranslate();
        MultipartFile importExampleTranFile = new MockMultipartFile("file.csv", "Test project name,,\nen_US,,\nkey,content,translation\ntranKey1,Witaj,Hello\ntest,Swiecie,World".getBytes());

        //when
        //then
        Exception exception = assertThrows(EntityNotFoundException.class, () -> importService.importTranslatorCSVFile(importExampleTranFile));
        assertTrue(exception.getMessage().contains("test"));
        assertEquals(0, translationRepository.findAll().size());
    }

    @Test
    @DisplayName("When translator file have wrong formatting, rollback transaction and return message")
    void whenWrongFormattingTranslatorCSVFileThenRollback() throws Exception {
        //given
        saveMessagesToTranslate();
        MultipartFile importExampleTranFile = new MockMultipartFile("file.csv", "Test project name;;\nen_US;;\nkey;content;translation\ntranKey1;Witaj;Hello\ntranKey2;Swiecie;World".getBytes());

        //when
        //then
        Exception exception = assertThrows(Exception.class, () -> importService.importTranslatorCSVFile(importExampleTranFile));
        assertTrue(exception.getMessage().contains(", or check if your delimiter is set to \",\" (comma)"));
        assertEquals(0, translationRepository.findAll().size());
    }

    @Test
    @DisplayName("When developer file have wrong formatting, rollback transaction and return message")
    void whenWrongFormattingDeveloperCSVFileThenRollback() {
        //given
        MultipartFile importExampleDevFile = new MockMultipartFile("file.csv", "Test project name;\nkey;content\ntestKey1;testContent1\ntestKey2;testContent2".getBytes());

        //when
        //then
        Exception exception = assertThrows(Exception.class, () -> importService.importDeveloperCSVMessage(importExampleDevFile));
        assertTrue(exception.getMessage().contains(", or check if your delimiter is set to \",\" (comma)"));
        assertEquals(0, messageRepository.findAll().size());
    }

    @Test
    @DisplayName("When developer file have wrong project name, rollback transaction and return message")
    void whenWrongProjectNameInDeveloperCSVFileThenRollback() {
        //given
        MultipartFile importExampleDevFile = new MockMultipartFile("file.csv", "Wrong project name,\nkey,content\ntestKey1,testContent1\ntestKey2,testContent2".getBytes());

        //when
        //then
        Exception exception = assertThrows(EntityNotFoundException.class, () -> importService.importDeveloperCSVMessage(importExampleDevFile));
        assertTrue(exception.getMessage().contains("Wrong project name"));
        assertEquals(0, messageRepository.findAll().size());
    }

    private void saveMessagesToTranslate() {
        Message messageOne = new Message("tranKey1", "Witaj", project);
        Message messageTwo = new Message("tranKey2", "Swiecie", project);

        messageRepository.saveAll(List.of(messageOne, messageTwo));
    }
}