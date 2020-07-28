package org.tim.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.LocaleUtils;
import org.junit.jupiter.api.DisplayName;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.tim.constants.CSVFileConstants.CSV_FILE_NAME;

@ExtendWith(MockitoExtension.class)
public class ExportServiceTestIT {

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
	@DisplayName("Throw exception if project doesn't exists")
	void generateExcelReport_projectNotFound_exception() {
		//given
		Mockito.lenient().when(projectRepository.findById(1L)).thenReturn(Optional.empty());
		var reportService = new ExportService(projectRepository, messageRepository, translationRepository);
		//when, then
		assertThrows(EntityNotFoundException.class, () -> {
			reportService.generateCSVReport(1L, new String[]{});
		});
	}

	@Test
	@DisplayName("Check if first message is generated correct")
	void generateCSVAndCheckIfFirsMessageIsCorrect() {
		//given
		testInitialize();
		var reportService = new ExportService(projectRepository, messageRepository, translationRepository);
		//when
		reportService.generateCSVReport(0L, new String[]{testLocale1, testLocale2, testLocale3, testLocale5});
		//then
		try {
			BufferedReader reader = Files.newBufferedReader(Paths.get(CSV_FILE_NAME));
			CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
			List<CSVRecord> records = parser.getRecords();

			assertAll(
					() -> assertEquals("", records.get(0).get(0)),
					() -> assertEquals("message Key", records.get(0).get(1)),
					() -> assertEquals("", records.get(1).get(0)),
					() -> assertEquals("message Content", records.get(1).get(1)),
					() -> assertEquals("Message1", records.get(1).get(2)),
					() -> assertEquals("", records.get(2).get(0)),
					() -> assertEquals("message Description", records.get(2).get(1)),
					() -> assertEquals("Locale", records.get(3).get(0)),
					() -> assertEquals("Translation Status", records.get(3).get(1)),
					() -> assertEquals("Translation", records.get(3).get(2)),
					() -> assertEquals("Substitute Locale", records.get(3).get(3)),
					() -> assertEquals("Substitute Translation", records.get(3).get(4)),
					() -> assertEquals("en_US", records.get(4).get(0)),
					() -> assertEquals("Invalid", records.get(4).get(1)),
					() -> assertEquals("Message1TranslationUS", records.get(4).get(2)),
					() -> assertEquals("New translation", records.get(5).get(1)),
					() -> assertEquals("", records.get(5).get(2))
			);

		} catch (IOException e) {
			fail();
		}
	}

	@Test
	@DisplayName("Check if correct substitute translation is added")
	void generateCSVAndCheckIfSubstituteTranslationIsAddedCorrect() {
		//given
		testInitialize();
		var reportService = new ExportService(projectRepository, messageRepository, translationRepository);
		//when
		reportService.generateCSVReport(0L, new String[]{testLocale1, testLocale2, testLocale3, testLocale5});
		//then
		try {
			BufferedReader reader = Files.newBufferedReader(Paths.get(CSV_FILE_NAME));
			CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
			List<CSVRecord> records = parser.getRecords();

			for (CSVRecord record : records) {
				if (record.get(1).equals("Missing")) {
					assertAll(
							() -> assertEquals("ru", record.get(3)),
							() -> assertEquals("Message1TranslationRU", record.get(4))
					);
					break;
				}
			}
		} catch (IOException e) {
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
