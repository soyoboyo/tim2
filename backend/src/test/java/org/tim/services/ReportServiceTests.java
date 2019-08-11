package org.tim.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tim.entities.Message;
import org.tim.entities.Project;
import org.tim.entities.Translation;
import org.tim.exceptions.EntityNotFoundException;
import org.tim.repositories.MessageRepository;
import org.tim.repositories.ProjectRepository;
import org.tim.repositories.TranslationRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.tim.constants.CSVFileConstants.CSV_FILE_NAME;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTests {

    @Mock
    ProjectRepository projectRepository;
    @Mock
    MessageRepository messageRepository;
    @Mock
    TranslationRepository translationRepository;
    @Mock
    Message message1;
    @Mock
    Message message2;

    private Locale testLocale1 = new Locale("PL");
    private Locale testLocale2 = new Locale("US");
    private Locale testLocale3 = new Locale("UK");
    private Locale testLocale4 = new Locale("RUS");

    @Test
    void generateExcelReport_projectNotFound_exception() {
        //given
        Mockito.lenient().when(projectRepository.findById(1L)).thenReturn(Optional.empty());
        var reportService = new ReportService(projectRepository, messageRepository, translationRepository);
        //when, then
        assertThrows(EntityNotFoundException.class, () -> {
            reportService.generateCSVReport(1L, new String[]{});
        });
    }


    @Test
    void generateExcelReport_validCall_correctCSVFileProduced() {
        //given
        testInitialize();
        var reportService = new ReportService(projectRepository, messageRepository, translationRepository);
        //when
        reportService.generateCSVReport(0L, new String[]{testLocale1.toString(), testLocale2.toString(), testLocale3.toString(), testLocale4.toString()});
        //then
        try {
            var reader = Files.newBufferedReader(Paths.get(CSV_FILE_NAME));
            var parser = new CSVParser(reader, CSVFormat.DEFAULT);
            var records = parser.getRecords();
            //Locales
            assertAll(
                    () -> assertEquals("Locale", records.get(0).get(0)),
                    () -> assertEquals("pl", records.get(1).get(0)),
                    () -> assertEquals("pl", records.get(2).get(0)),
                    () -> assertEquals("us", records.get(3).get(0)),
                    () -> assertEquals("us", records.get(4).get(0)),
                    () -> assertEquals("uk", records.get(5).get(0)),
                    () -> assertEquals("uk", records.get(6).get(0)),
                    () -> assertEquals("rus", records.get(7).get(0)),
                    () -> assertEquals("rus", records.get(8).get(0)),
                    //Message content
                    () -> assertEquals("Message", records.get(0).get(1)),
                    () -> assertEquals("Message1", records.get(1).get(1)),
                    () -> assertEquals("Message2", records.get(2).get(1)),
                    () -> assertEquals("Message1", records.get(3).get(1)),
                    () -> assertEquals("Message2", records.get(4).get(1)),
                    () -> assertEquals("Message1", records.get(5).get(1)),
                    () -> assertEquals("Message2", records.get(6).get(1)),
                    () -> assertEquals("Message1", records.get(7).get(1)),
                    () -> assertEquals("Message2", records.get(8).get(1)),
                    //Translation Status
                    () -> assertEquals("Translation Status", records.get(0).get(2)),
                    () -> assertEquals("Valid", records.get(1).get(2)),
                    () -> assertEquals("Valid", records.get(2).get(2)),
                    () -> assertEquals("Invalid", records.get(3).get(2)),
                    () -> assertEquals("Invalid", records.get(4).get(2)),
                    () -> assertEquals("Outdated", records.get(5).get(2)),
                    () -> assertEquals("Outdated", records.get(6).get(2)),
                    () -> assertEquals("Missing", records.get(7).get(2)),
                    () -> assertEquals("Missing", records.get(8).get(2)),
                    //Translation
                    () -> assertEquals("Translation", records.get(0).get(3)),
                    () -> assertEquals("Message1TranslationPL", records.get(1).get(3)),
                    () -> assertEquals("Message2TranslationPL", records.get(2).get(3)),
                    () -> assertEquals("Message1TranslationUS", records.get(3).get(3)),
                    () -> assertEquals("Message2TranslationUS", records.get(4).get(3)),
                    () -> assertEquals("Message1TranslationUK", records.get(5).get(3)),
                    () -> assertEquals("Message2TranslationUK", records.get(6).get(3)),
                    () -> assertEquals(StringUtils.EMPTY, records.get(7).get(3)),
                    () -> assertEquals(StringUtils.EMPTY, records.get(8).get(3))
            );
        } catch (IOException ex) {
            fail();
        }
    }

    private void testInitialize() {
        var project = new Project("Name", new Locale("xd"));
        Mockito.lenient().when(projectRepository.findById(0L)).thenReturn(Optional.of(project));
        Mockito.lenient().when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        Mockito.when(message1.getContent()).thenReturn(("Message1"));
        Mockito.when(message2.getContent()).thenReturn(("Message2"));

        Mockito.when(messageRepository.findMessagesByProjectIdAndIsArchivedFalse(0L)).thenReturn(new LinkedList<>(Arrays.asList(message1, message2)));

        var translationPLToMessage1 = new Translation();
        translationPLToMessage1.setContent("Message1TranslationPL");
        translationPLToMessage1.setIsValid(true);
        translationPLToMessage1.setMessage(message1);
        translationPLToMessage1.setIsArchived(false);
        translationPLToMessage1.setLocale(testLocale1);

        var translationUSToMessage1 = new Translation();
        translationUSToMessage1.setContent("Message1TranslationUS");
        translationUSToMessage1.setIsValid(false);
        translationUSToMessage1.setMessage(message1);
        translationUSToMessage1.setIsArchived(false);
        translationUSToMessage1.setLocale(testLocale2);

        var translationUKToMessage1 = new Translation();
        translationUKToMessage1.setContent("Message1TranslationUK");
        translationUKToMessage1.setIsValid(true);
        translationUKToMessage1.setMessage(message1);
        translationUKToMessage1.setIsArchived(false);
        translationUKToMessage1.setLocale(testLocale3);

        var translationPLToMessage2 = new Translation();
        translationPLToMessage2.setContent("Message2TranslationPL");
        translationPLToMessage2.setIsValid(true);
        translationPLToMessage2.setMessage(message2);
        translationPLToMessage2.setIsArchived(false);
        translationPLToMessage2.setLocale(testLocale1);

        var translationUSToMessage2 = new Translation();
        translationUSToMessage2.setContent("Message2TranslationUS");
        translationUSToMessage2.setIsValid(false);
        translationUSToMessage2.setMessage(message2);
        translationUSToMessage2.setIsArchived(false);
        translationUSToMessage2.setLocale(testLocale2);

        var translationUKToMessage2 = new Translation();
        translationUKToMessage2.setContent("Message2TranslationUK");
        translationUKToMessage2.setIsValid(true);
        translationUKToMessage2.setMessage(message2);
        translationUKToMessage2.setIsArchived(false);
        translationUKToMessage2.setLocale(testLocale3);

        Mockito.when(translationRepository.findTranslationsByLocaleAndMessage(testLocale1, message1)).thenReturn(Optional.of(translationPLToMessage1));
        Mockito.when(translationRepository.findTranslationsByLocaleAndMessage(testLocale2, message1)).thenReturn(Optional.of(translationUSToMessage1));
        Mockito.when(translationRepository.findTranslationsByLocaleAndMessage(testLocale3, message1)).thenReturn(Optional.of(translationUKToMessage1));

        Mockito.when(translationRepository.findTranslationsByLocaleAndMessage(testLocale1, message2)).thenReturn(Optional.of(translationPLToMessage2));
        Mockito.when(translationRepository.findTranslationsByLocaleAndMessage(testLocale2, message2)).thenReturn(Optional.of(translationUSToMessage2));
        Mockito.when(translationRepository.findTranslationsByLocaleAndMessage(testLocale3, message2)).thenReturn(Optional.of(translationUKToMessage2));

        Mockito.when(message1.isTranslationOutdated(translationPLToMessage1)).thenReturn(false);
        Mockito.when(message1.isTranslationOutdated(translationUSToMessage1)).thenReturn(false);
        Mockito.when(message1.isTranslationOutdated(translationUKToMessage1)).thenReturn(true);

        Mockito.when(message2.isTranslationOutdated(translationPLToMessage2)).thenReturn(false);
        Mockito.when(message2.isTranslationOutdated(translationUSToMessage2)).thenReturn(false);
        Mockito.when(message2.isTranslationOutdated(translationUKToMessage2)).thenReturn(true);
    }
}
