package org.tim.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.tim.configuration.SpringTestsCustomExtension;
import org.tim.entities.LocaleWrapper;
import org.tim.entities.Message;
import org.tim.entities.Project;
import org.tim.entities.Translation;
import org.tim.exceptions.ValidationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CDExportsServiceTest extends SpringTestsCustomExtension {

	@Autowired
	private CDExportsService cdExportsService;

	private Project project;

	@BeforeEach
	void setUp() {
		project = createEmptyGermanToEnglishProject();
		createAndSaveMessages(project);
		createAndSaveTranslationsToEnglish();
	}

	@Test
	@DisplayName("Return all ready translations for project source locale")
	void whenExportingProjectBySourceLocaleThenReturnExistingMessages() {
		//given
		List<Message> messages = createMessages(project);
		String[] expectedMessageValues = messages.stream().map(Message::getContent).toArray(String[]::new);

		//when
		Map<String, String> exported = cdExportsService.exportAllReadyTranslationsByProjectAndByLocale(project.getId(), Locale.GERMAN.toString());
		//then
		assertAll(
				() -> assertEquals(3, exported.size()),
				() -> assertThat(exported).containsValues(expectedMessageValues)
		);
	}

	@Test
	@DisplayName("Return all ready translations for project target locale")
	void whenExportingProjectByTargetLocaleThenReturnExistingTranslations() {
		//given
		List<Translation> translations = createTranslationsToEnglish();
		String[] expectedTranslations = translations.stream().map(Translation::getContent).toArray(String[]::new);

		//when
		Map<String, String> exported = cdExportsService.exportAllReadyTranslationsByProjectAndByLocale(project.getId(), Locale.ENGLISH.toString());

		//then
		assertAll(
				() -> assertEquals(3, exported.size()),
				() -> assertThat(exported).containsValues(expectedTranslations)
		);
	}

	@Test
	@DisplayName("Generate ZIP with translation files")
	void whenExportingFileThenReturnZIPWithTranslations() throws IOException {
		//given
		List<String> translationsForProject = List.of("en.json", "de.json");
		long projectID = project.getId();

		//when
		byte[] bytes = cdExportsService.exportAllReadyTranslationsByProjectInZIP(projectID, new MockHttpServletResponse());
		ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(bytes));
		List<String> fileNamesInZip = getFileNames(zipInputStream);

		//then
		assertThat(fileNamesInZip).containsExactlyInAnyOrder(translationsForProject.toArray(String[]::new));
	}

	@Test
	@DisplayName("Check if file in ZIP contains translations")
	void correctFileWithTranslationsInZIPGenerated() throws IOException {
		//given
		long projectID = project.getId();
		List<Map<String, String>> translations = getTranslations();

		//when
		byte[] bytes = cdExportsService.exportAllReadyTranslationsByProjectInZIP(projectID, new MockHttpServletResponse());
		ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(bytes));
		List<Map<String, String>> translationsFromFile = getTranslationsFromZIPFiles(zipInputStream);

		//then
		assertThat(translationsFromFile).containsAll(translations);
	}

	@Test
	@DisplayName("Return messages for project and translations for only fully translated locales")
	void whenExportingProjectByTargetLocaleThenReturnAllFullyTranslatedLocales() throws IOException {
		//given
		updateProjectWithEmptyTranslations();
		long projectID = project.getId();
		List<Map<String, String>> expectedTranslations = expectedTranslations();

		//when
		byte[] bytes = cdExportsService.exportAllReadyTranslationsByProjectInZIP(projectID, new MockHttpServletResponse());
		ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(bytes));
		List<Map<String, String>> translationsFromFile = getTranslationsFromZIPFiles(zipInputStream);

		//then
		assertThat(translationsFromFile).containsAll(expectedTranslations);
	}

	@Test
	@DisplayName("Throw validation error during exporting translation when locale were sent in wrong format.")
	public void whenExportAllReadyTranslationsByProjectAndByLocaleAndLocaleInWrongFormatThenThrowException() {
		// given
		String locale = "ag_XX7d9ww";

		// then
		assertThrows(ValidationException.class, () -> {
			cdExportsService.exportAllReadyTranslationsByProjectAndByLocale(project.getId(), locale); // when
		}, "Locale: " + locale + " doesn't exist.");
	}

	private List<Map<String, String>> getTranslations() {
		List<Message> messages = createMessages(project);
		List<Translation> translationsToEnglish = createTranslationsToEnglish();

		List<Map<String, String>> translations = new ArrayList<>();
		translations.add(messages.stream().collect(Collectors.toMap(Message::getKey, Message::getContent)));
		translations.add(translationsToEnglish.stream().collect(Collectors.toMap(translation -> translation.getMessage().getKey(), Translation::getContent)));

		return translations;
	}

	private List<Map<String, String>> getTranslationsFromZIPFiles(ZipInputStream zipInputStream) throws IOException {
		List<Map<String, String>> translations = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();

		ZipEntry entry;
		while ((entry = zipInputStream.getNextEntry()) != null) {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byteArrayOutputStream.write(zipInputStream.readAllBytes());

			TypeReference<Map<String, String>> typeReference = new TypeReference<>() {
			};

			translations.add(objectMapper.readValue(byteArrayOutputStream.toByteArray(), typeReference));
			byteArrayOutputStream.close();
		}

		return translations;
	}

	private List<String> getFileNames(ZipInputStream zipInputStream) throws IOException {
		List<String> fileNames = new ArrayList<>();
		ZipEntry zipEntry;

		while ((zipEntry = zipInputStream.getNextEntry()) != null) {
			fileNames.add(zipEntry.getName());
		}

		return fileNames;
	}

	private void createAndSaveMessages(Project project) {
		messageRepository.saveAll(createMessages(project));
	}

	private List<Message> createMessages(Project project) {
		Message messageM1 = new Message("test1", "test1", project);
		Message messageM2 = new Message("test2", "test2", project);
		Message messageM3 = new Message("test3", "test3", project);

		return List.of(messageM1, messageM2, messageM3);
	}

	private void createAndSaveTranslationsToEnglish() {
		translationRepository.saveAll(createTranslationsToEnglish());
	}

	private List<Translation> createTranslationsToEnglish() {
		List<Message> messages = messageRepository.findAll();
		List<Translation> translations = new ArrayList<>();

		for (Message message : messages) {
			Translation translation = new Translation();
			translation.setIsValid(true);
			translation.setLocale(Locale.ENGLISH);
			translation.setContent(message.getContent() + "translationEN");
			translation.setMessage(message);

			translations.add(translation);
		}

		return translations;
	}

	private List<Map<String, String>> expectedTranslations() {
		HashMap<String, String> messages = new HashMap<>();
		messages.put("test1", "test1");
		messages.put("test2", "test2");
		messages.put("test3", "test3");
		messages.put("test4", "test4");
		HashMap<String, String> translationsDE = new HashMap<>();
		translationsDE.put("test1", "test1translationDE");
		translationsDE.put("test2", "test2translationDE");
		translationsDE.put("test3", "test3translationDE");
		translationsDE.put("test4", "test4translationDE");

		return List.of(messages, translationsDE);
	}

	private void updateProjectWithEmptyTranslations() {
		project.addTargetLocale(List.of(new LocaleWrapper(Locale.GERMANY)));
		projectRepository.save(project);

		messageRepository.save(new Message("test4", "test4", project));

		List<Message> messages = messageRepository.findAll();
		List<Translation> translations = new ArrayList<>();

		for (Message message : messages) {
			Translation translation = new Translation();
			translation.setIsValid(true);
			translation.setLocale(Locale.GERMANY);
			translation.setContent(message.getContent() + "translationDE");
			translation.setMessage(message);

			translations.add(translation);
		}

		translationRepository.saveAll(translations);
	}
}