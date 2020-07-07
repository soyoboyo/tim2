package org.tim.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.tim.configuration.SpringTestsCustomExtension;
import org.tim.entities.Project;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ImportServiceTestIT extends SpringTestsCustomExtension {

    @Autowired
    private ImportService importService;

    private Project project;

    private MultipartFile importExampleFile = new MockMultipartFile("file.csv", "Test project name,\nkey,content\ntestKey1,testContent1\ntestKey2,testContent2".getBytes());

    @BeforeEach
    void setUp() {
        project = createEmptyGermanToEnglishProject();
        project.setName("Test project name");
        projectRepository.save(project);
    }

    @Test
    void whenCorrectFileImportedThenMessagesAreCreated() {
        //given
        //when
        importService.importDeveloperCSVMessage(importExampleFile);
        //then
        assertEquals(2, messageRepository.findAll().size());
    }
}