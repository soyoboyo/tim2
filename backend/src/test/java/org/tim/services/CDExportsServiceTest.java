package org.tim.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tim.configuration.SpringTestsCustomExtension;
import org.tim.entities.Message;
import org.tim.entities.Project;
import org.tim.entities.Translation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
			translation.setContent(message.getContent() + "translation");
			translation.setMessage(message);

			translations.add(translation);
		}

		return translations;
	}
}