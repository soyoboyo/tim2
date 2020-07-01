package org.tim.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tim.entities.LocaleWrapper;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.tim.constants.CSVFileConstants.CSV_FILE_NAME;
import static org.tim.constants.CSVFileConstants.STD_HEADERS;

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

    private String testLocale1 = "pl";
    private String testLocale2 = "en_US";
    private String testLocale3 = "en_GB";
    private String testLocale4 = "ru";
    private String testLocale5 = "hr";

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
        reportService.generateCSVReport(0L, new String[]{testLocale1, testLocale2, testLocale3, testLocale5});
        //then
        try {
            var reader = Files.newBufferedReader(Paths.get(CSV_FILE_NAME));
            var parser = new CSVParser(reader, CSVFormat.DEFAULT);
            var records = parser.getRecords();
            assertAll(
                    //message Header 1
                    () -> assertEquals("", records.get(0).get(0)),
                    () -> assertEquals("message Key", records.get(0).get(1)),
                    () -> assertEquals("", records.get(0).get(2)),
                    () -> assertEquals("", records.get(1).get(0)),
                    () -> assertEquals("message Content", records.get(1).get(1)),
                    () -> assertEquals("Message1", records.get(1).get(2)),
                    () -> assertEquals("", records.get(2).get(0)),
                    () -> assertEquals("message Description", records.get(2).get(1)),
                    () -> assertEquals("", records.get(2).get(2)),
                    //Cell Headers
                    () -> assertEquals(STD_HEADERS[0], records.get(3).get(0)),
                    () -> assertEquals(STD_HEADERS[1], records.get(3).get(1)),
                    () -> assertEquals(STD_HEADERS[2], records.get(3).get(2)),
                    () -> assertEquals(STD_HEADERS[3], records.get(3).get(3)),
                    () -> assertEquals(STD_HEADERS[4], records.get(3).get(4)),
                    //Message1 TranslationUS
                    () -> assertEquals(testLocale2, records.get(4).get(0)),
                    () -> assertEquals("Invalid", records.get(4).get(1)),
                    () -> assertEquals("Message1TranslationUS", records.get(4).get(2)),
                    () -> assertEquals("-", records.get(4).get(3)),
                    () -> assertEquals("-", records.get(4).get(4)),
                    () -> assertEquals("New translation", records.get(5).get(0)),
                    () -> assertEquals("-", records.get(5).get(1)),
                    //Message1 TranslationUK
                    () -> assertEquals(testLocale3, records.get(6).get(0)),
                    () -> assertEquals("Outdated", records.get(6).get(1)),
                    () -> assertEquals("Message1TranslationUK", records.get(6).get(2)),
                    () -> assertEquals("-", records.get(6).get(3)),
                    () -> assertEquals("-", records.get(6).get(4)),
                    () -> assertEquals("New translation", records.get(7).get(0)),
                    () -> assertEquals("-", records.get(7).get(1)),
                    //Message1 TranslationHR
                    () -> assertEquals(testLocale5, records.get(8).get(0)),
                    () -> assertEquals("Missing", records.get(8).get(1)),
                    () -> assertEquals("-", records.get(8).get(2)),
                    () -> assertEquals("ru", records.get(8).get(3)),
                    () -> assertEquals("Message1TranslationRU", records.get(8).get(4)),
                    () -> assertEquals("New translation", records.get(9).get(0)),
                    () -> assertEquals("-", records.get(9).get(1)),
                    //message Header 2
                    () -> assertEquals("", records.get(10).get(0)),
                    () -> assertEquals("message Key", records.get(10).get(1)),
                    () -> assertEquals("", records.get(10).get(2)),
                    () -> assertEquals("", records.get(11).get(0)),
                    () -> assertEquals("message Content", records.get(11).get(1)),
                    () -> assertEquals("Message2", records.get(11).get(2)),
                    () -> assertEquals("", records.get(12).get(0)),
                    () -> assertEquals("message Description", records.get(12).get(1)),
                    () -> assertEquals("", records.get(12).get(2)),
                    //Cell Headers
                    () -> assertEquals(STD_HEADERS[0], records.get(13).get(0)),
                    () -> assertEquals(STD_HEADERS[1], records.get(13).get(1)),
                    () -> assertEquals(STD_HEADERS[2], records.get(13).get(2)),
                    () -> assertEquals(STD_HEADERS[3], records.get(13).get(3)),
                    () -> assertEquals(STD_HEADERS[4], records.get(13).get(4)),
                    //Message2 TranslationUS
                    () -> assertEquals(testLocale2, records.get(14).get(0)),
                    () -> assertEquals("Invalid", records.get(14).get(1)),
                    () -> assertEquals("Message2TranslationUS", records.get(14).get(2)),
                    () -> assertEquals("-", records.get(14).get(3)),
                    () -> assertEquals("-", records.get(14).get(4)),
                    () -> assertEquals("New translation", records.get(15).get(0)),
                    () -> assertEquals("-", records.get(15).get(1)),
                    //Message2 TranslationUK
                    () -> assertEquals(testLocale3, records.get(16).get(0)),
                    () -> assertEquals("Outdated", records.get(16).get(1)),
                    () -> assertEquals("Message2TranslationUK", records.get(16).get(2)),
                    () -> assertEquals("-", records.get(16).get(3)),
                    () -> assertEquals("-", records.get(16).get(4)),
                    () -> assertEquals("New translation", records.get(17).get(0)),
                    () -> assertEquals("-", records.get(17).get(1)),
                    //Message2 TranslationHR
                    () -> assertEquals(testLocale5, records.get(18).get(0)),
                    () -> assertEquals("Missing", records.get(18).get(1)),
                    () -> assertEquals("-", records.get(18).get(2)),
                    () -> assertEquals("ru", records.get(18).get(3)),
                    () -> assertEquals("Message2TranslationRU", records.get(18).get(4)),
                    () -> assertEquals("New translation", records.get(19).get(0)),
                    () -> assertEquals("-", records.get(19).get(1))



            );

        } catch (IOException ex) {
            fail();
        }
    }

    private void testInitialize() {
        var project = new Project("ProjectName", new Locale("en"));
        var list = new ArrayList<LocaleWrapper>();
        list.add(new LocaleWrapper(new Locale("hr")));
        list.add(new LocaleWrapper(new Locale("ru")));
        project.addTargetLocale(list);
        project.updateSubstituteLocale(new LocaleWrapper(new Locale("hr")), new LocaleWrapper(new Locale("ru")));

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
        translationPLToMessage1.setLocale(LocaleUtils.toLocale(testLocale1));

        var translationUSToMessage1 = new Translation();
        translationUSToMessage1.setContent("Message1TranslationUS");
        translationUSToMessage1.setIsValid(false);
        translationUSToMessage1.setMessage(message1);
        translationUSToMessage1.setIsArchived(false);
        translationUSToMessage1.setLocale(LocaleUtils.toLocale(testLocale2));

        var translationUKToMessage1 = new Translation();
        translationUKToMessage1.setContent("Message1TranslationUK");
        translationUKToMessage1.setIsValid(true);
        translationUKToMessage1.setMessage(message1);
        translationUKToMessage1.setIsArchived(false);
        translationUKToMessage1.setLocale(LocaleUtils.toLocale(testLocale3));

        var translationRUToMessage1 = new Translation();
        translationRUToMessage1.setContent("Message1TranslationRU");
        translationRUToMessage1.setIsValid(true);
        translationRUToMessage1.setMessage(message1);
        translationRUToMessage1.setIsArchived(false);
        translationRUToMessage1.setLocale(LocaleUtils.toLocale(testLocale4));

        var translationPLToMessage2 = new Translation();
        translationPLToMessage2.setContent("Message2TranslationPL");
        translationPLToMessage2.setIsValid(true);
        translationPLToMessage2.setMessage(message2);
        translationPLToMessage2.setIsArchived(false);
        translationPLToMessage2.setLocale(LocaleUtils.toLocale(testLocale1));

        var translationUSToMessage2 = new Translation();
        translationUSToMessage2.setContent("Message2TranslationUS");
        translationUSToMessage2.setIsValid(false);
        translationUSToMessage2.setMessage(message2);
        translationUSToMessage2.setIsArchived(false);
        translationUSToMessage2.setLocale(LocaleUtils.toLocale(testLocale2));

        var translationUKToMessage2 = new Translation();
        translationUKToMessage2.setContent("Message2TranslationUK");
        translationUKToMessage2.setIsValid(true);
        translationUKToMessage2.setMessage(message2);
        translationUKToMessage2.setIsArchived(false);
        translationUKToMessage2.setLocale(LocaleUtils.toLocale(testLocale3));

        var translationRUToMessage2 = new Translation();
        translationRUToMessage2.setContent("Message2TranslationRU");
        translationRUToMessage2.setIsValid(true);
        translationRUToMessage2.setMessage(message2);
        translationRUToMessage2.setIsArchived(false);
        translationRUToMessage2.setLocale(LocaleUtils.toLocale(testLocale3));

        Mockito.when(translationRepository.findTranslationsByLocaleAndMessage(LocaleUtils.toLocale(testLocale1), message1)).thenReturn(Optional.of(translationPLToMessage1));
        Mockito.when(translationRepository.findTranslationsByLocaleAndMessage(LocaleUtils.toLocale(testLocale2), message1)).thenReturn(Optional.of(translationUSToMessage1));
        Mockito.when(translationRepository.findTranslationsByLocaleAndMessage(LocaleUtils.toLocale(testLocale3), message1)).thenReturn(Optional.of(translationUKToMessage1));
        Mockito.when(translationRepository.findTranslationsByLocaleAndMessage(LocaleUtils.toLocale(testLocale5), message1)).thenReturn(Optional.empty());
        Mockito.when(translationRepository.findTranslationsByLocaleAndMessage(LocaleUtils.toLocale(testLocale4), message1)).thenReturn(Optional.of(translationRUToMessage1));

        Mockito.when(translationRepository.findTranslationsByLocaleAndMessage(LocaleUtils.toLocale(testLocale1), message2)).thenReturn(Optional.of(translationPLToMessage2));
        Mockito.when(translationRepository.findTranslationsByLocaleAndMessage(LocaleUtils.toLocale(testLocale2), message2)).thenReturn(Optional.of(translationUSToMessage2));
        Mockito.when(translationRepository.findTranslationsByLocaleAndMessage(LocaleUtils.toLocale(testLocale3), message2)).thenReturn(Optional.of(translationUKToMessage2));
        Mockito.when(translationRepository.findTranslationsByLocaleAndMessage(LocaleUtils.toLocale(testLocale5), message2)).thenReturn(Optional.empty());
        Mockito.when(translationRepository.findTranslationsByLocaleAndMessage(LocaleUtils.toLocale(testLocale4), message2)).thenReturn(Optional.of(translationRUToMessage2));

        Mockito.when(message1.isTranslationOutdated(translationPLToMessage1)).thenReturn(false);
        Mockito.when(message1.isTranslationOutdated(translationUSToMessage1)).thenReturn(false);
        Mockito.when(message1.isTranslationOutdated(translationUKToMessage1)).thenReturn(true);

        Mockito.when(message2.isTranslationOutdated(translationPLToMessage2)).thenReturn(false);
        Mockito.when(message2.isTranslationOutdated(translationUSToMessage2)).thenReturn(false);
        Mockito.when(message2.isTranslationOutdated(translationUKToMessage2)).thenReturn(true);
    }
}
