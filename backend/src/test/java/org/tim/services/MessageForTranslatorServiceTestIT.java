package org.tim.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.tim.DTOs.MessageDTO;
import org.tim.DTOs.input.TranslationCreateDTO;
import org.tim.DTOs.output.MessageForTranslator;
import org.tim.DTOs.output.TranslationForTranslator;
import org.tim.configuration.SpringTestsCustomExtension;
import org.tim.entities.Message;
import org.tim.entities.Project;
import org.tim.entities.Translation;
import org.tim.exceptions.ValidationException;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.junit.jupiter.api.Assertions.*;

public class MessageForTranslatorServiceTestIT extends SpringTestsCustomExtension {

	private Project project;
	private final ModelMapper mapper = new ModelMapper();

	@Autowired
	private MessageForTranslatorService messageForTranslatorService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private TranslationService translationService;

	@BeforeEach
	void setUp() {
		project = createEmptyGermanToEnglishProjectWithTwoSubstituteLocales();
	}

	@Test
	@DisplayName("Return all existing message for translator.")
	void whenGettingMessageForTranslatorThenReturnMessagesForTranslator() {
		// given
		MessageDTO messageDTO = random(MessageDTO.class);
		messageDTO.setProjectId(project.getId());
		messageService.createMessage(messageDTO);
		// when
		List<MessageForTranslator> messages = messageForTranslatorService.getMessagesForTranslator(project.getId(), "ab_CD");
		// then
		assertAll(
				() -> assertEquals(1, messages.size())
		);
	}

	@Test
	@DisplayName("Return all existing translation without translation and substitude when locale not sent.")
	void whenGetMessageForTranslationWithoutLocaleThenReturnMessagesWithoutTranslationAndSubstitute() {
		Long projectId = projectService.getAllProjects().get(0).getId();
		Message message = messageService.createMessage(new MessageDTO("key", "it's message", projectId));
		TranslationCreateDTO translationCreateDTO = new TranslationCreateDTO();
		translationCreateDTO.setLocale(Locale.UK.toString());
		String translationContent = "it's content";
		translationCreateDTO.setContent(translationContent);
		translationService.createTranslation(translationCreateDTO, message.getId());
		// when
		List<MessageForTranslator> messageForTranslators = messageForTranslatorService.getMessagesForTranslator(projectId);
		// then
		assertAll(
				() -> assertNull(messageForTranslators.get(0).getTranslation()),
				() -> assertNull(messageForTranslators.get(0).getSubstitute())
		);
	}

	@Test
	@DisplayName("As a Translator I see messages without translations but ordered correctly.")
	void whenGetMessageForTranslationWithoutLocaleThenReturnMessagesWithoutTranslationAndSubstituteOrderedCorrectly() throws InterruptedException {
		//given
		Project project = projectService.getAllProjects().get(0);
		List<Message> messages = createMessagesForTestsWithDelay(project);
		// when
		List<MessageForTranslator> messageForTranslators = messageForTranslatorService.getMessagesForTranslator(project.getId());
		// then
		assertAll(
				() -> assertEquals(messages.get(2).getKey(), messageForTranslators.get(0).getKey()),
				() -> assertEquals(messages.get(1).getKey(), messageForTranslators.get(1).getKey()),
				() -> assertEquals(messages.get(0).getKey(), messageForTranslators.get(2).getKey())
		);
	}

	@Test
	@DisplayName("As a Translator I see messages without translations but ordered correctly.")
	void whenGetMessageForTranslationWithoutLocaleAndMessagesWereUpdatedThenReturnMessagesOrderedCorrectly() throws InterruptedException {
		//given
		Project project = projectService.getAllProjects().get(0);
		List<Message> messages = createMessagesForTestsWithDelay(project);
		updateMessage(messageRepository.findByKey(messages.get(1).getKey()).orElseThrow());

		// when
		List<MessageForTranslator> messageForTranslators = messageForTranslatorService.getMessagesForTranslator(project.getId());
		// then
		assertAll(
				() -> assertEquals(messageForTranslators.get(0).getKey(), messages.get(1).getKey()),
				() -> assertEquals(messageForTranslators.get(1).getKey(), messages.get(2).getKey()),
				() -> assertEquals(messageForTranslators.get(2).getKey(), messages.get(0).getKey())
		);
	}

	@Test
	@DisplayName("Return message without translation and substitute when both not exists.")
	void whenGetMessageAndTranslationAndSubstituteNotExistThenReturnMessagesWithoutTranslationAndSubstitute() {
		Long projectId = projectService.getAllProjects().get(0).getId();
		Message message = messageService.createMessage(new MessageDTO("key", "it's message", projectId));
		// when
		List<MessageForTranslator> messageForTranslators = messageForTranslatorService.getMessagesForTranslator(projectId, Locale.ENGLISH.toString());
		// then
		assertAll(
				() -> assertNull(messageForTranslators.get(0).getTranslation()),
				() -> assertNull(messageForTranslators.get(0).getSubstitute())
		);
	}

	@Test
	@DisplayName("As a Translator I see messages with translations and ordered correctly.")
	void whenGetMessageForTranslationWithLocaleAndTranslationExistThenReturnMessagesOrderedCorrectly() throws InterruptedException {
		//given
		Project project = projectService.getAllProjects().get(0);
		List<Message> messages = createMessagesForTestsWithDelay(project);
		Translation t1 = new Translation(Locale.GERMANY, messages.get(2));
		t1.setContent("Inhalt1");
		Translation t2 = new Translation(Locale.GERMANY, messages.get(1));
		t2.setContent("Inhalt2");
		Translation t3 = new Translation(Locale.GERMANY, messages.get(0));
		t3.setContent("Inhalt3");
		List<Translation> translations = Arrays.asList(t1, t2, t3);
		translationRepository.saveAll(translations);
		// when
		List<MessageForTranslator> messageForTranslators = messageForTranslatorService.getMessagesForTranslator(project.getId(), "de_DE");
		// then
		assertAll(
				() -> assertEquals(messageForTranslators.get(0).getKey(), messages.get(0).getKey()),
				() -> assertEquals(messageForTranslators.get(1).getKey(), messages.get(1).getKey()),
				() -> assertEquals(messageForTranslators.get(2).getKey(), messages.get(2).getKey())
		);
	}

	@Test
	@DisplayName("As a Translator I see messages with translations and ordered correctly.")
	void whenGetMessageForTranslationWithLocaleAndTranslationPartlyExistAndMessagesWereUpdatedThenReturnMessagesOrderedCorrectly() throws InterruptedException {
		//given
		Project project = projectService.getAllProjects().get(0);
		List<Message> messages = createMessagesForTestsWithDelay(project);
		updateMessage(messageRepository.findByKey(messages.get(1).getKey()).orElseThrow());
		List<Message> messagesAfterUpdated = messageRepository.findAll();
		messagesAfterUpdated.sort(Comparator.comparing(Message::getUpdateDate, Comparator.reverseOrder()));

		Translation t1 = new Translation(Locale.GERMANY, messages.get(2));
		t1.setContent("Inhalt1");
		Translation t3 = new Translation(Locale.GERMANY, messages.get(0));
		t3.setContent("Inhalt3");
		List<Translation> translations = Arrays.asList(t1, t3);
		translationRepository.saveAll(translations);

		// when
		List<MessageForTranslator> messageForTranslators = messageForTranslatorService.getMessagesForTranslator(project.getId(), "de_DE");
		// then
		assertAll(
				() -> assertEquals(messageForTranslators.get(0).getKey(), messagesAfterUpdated.get(0).getKey()),
				() -> assertEquals(messageForTranslators.get(1).getKey(), messagesAfterUpdated.get(1).getKey()),
				() -> assertEquals(messageForTranslators.get(2).getKey(), messagesAfterUpdated.get(2).getKey())
		);
	}

	@Test
	@DisplayName("Return message with substitute when translation not exists.")
	void whenGetMessageAndTranslationNotExistButSubstituteExistThenReturnOnlySubstitute() {
		Long projectId = projectService.getAllProjects().get(0).getId();
		Message message = messageService.createMessage(new MessageDTO("key", "it's message", projectId));
		TranslationCreateDTO translationCreateDTO = new TranslationCreateDTO();
		translationCreateDTO.setLocale(Locale.UK.toString());
		String translationContent = "it's content";
		translationCreateDTO.setContent(translationContent);
		translationService.createTranslation(translationCreateDTO, message.getId());
		// when
		List<MessageForTranslator> messageForTranslators = messageForTranslatorService.getMessagesForTranslator(projectId, Locale.ENGLISH.toString());
		// then
		assertAll(
				() -> assertNull(messageForTranslators.get(0).getTranslation()),
				() -> assertEquals(Locale.UK.toString(), messageForTranslators.get(0).getSubstitute().getLocale()),
				() -> assertEquals(translationContent, messageForTranslators.get(0).getSubstitute().getContent()),
				() -> assertEquals("key", messageForTranslators.get(0).getKey())
		);
	}

	@Test
	@DisplayName("Return message with substitute second level when translation not exists.")
	void whenGetMessageAndTranslationNotExistAndSubstituteExistOnSecondLevelReturnSubstitute() {
		Long projectId = projectService.getAllProjects().get(0).getId();
		Message message = messageService.createMessage(new MessageDTO("key", "it's message", projectId));
		TranslationCreateDTO translationCreateDTO = new TranslationCreateDTO();
		translationCreateDTO.setLocale(Locale.UK.toString());
		String translationContent = "it's content";
		translationCreateDTO.setContent(translationContent);
		translationService.createTranslation(translationCreateDTO, message.getId());
		// when
		List<MessageForTranslator> messageForTranslators = messageForTranslatorService.getMessagesForTranslator(projectId, Locale.ENGLISH.toString());
		// then
		assertAll(
				() -> assertNull(messageForTranslators.get(0).getTranslation()),
				() -> assertEquals(Locale.UK.toString(), messageForTranslators.get(0).getSubstitute().getLocale()),
				() -> assertEquals(translationContent, messageForTranslators.get(0).getSubstitute().getContent()),
				() -> assertEquals("key", messageForTranslators.get(0).getKey())
		);

	}

	@Test
	@DisplayName("Throw validation error when locale sent in wrong format when calling get translations.")
	void whenGetMessageAndLocaleSetWithWrongFormatThrowValidationException() {
		Long projectId = projectService.getAllProjects().get(0).getId();
		Message message = messageService.createMessage(new MessageDTO("key", "it's message", projectId));
		TranslationCreateDTO translationCreateDTO = new TranslationCreateDTO();
		translationCreateDTO.setLocale(Locale.UK.toString());
		String translationContent = "it's content";
		translationCreateDTO.setContent(translationContent);
		translationService.createTranslation(translationCreateDTO, message.getId());
		// when
		Exception exception = assertThrows(ValidationException.class, () ->
				messageForTranslatorService.getMessagesForTranslator(projectId, "wrong_format"));
		// then
		assertTrue(exception.getMessage().contains("Source locale: " +
				"wrong_format was given in the wrong format."));
	}

	@Test
	@DisplayName("As a Translator I see previous content of an updated Message.")
	void whenTranslationIsOutdated_thenGetPreviousMessageContent() {
		// given
		Long projectId = projectService.getAllProjects().get(0).getId();
		MessageDTO messageDTO = random(MessageDTO.class);
		messageDTO.setProjectId(projectId);
		messageDTO.setContent("Witam");
		Message message = messageService.createMessage(messageDTO);
		TranslationCreateDTO translationCreateDTO = new TranslationCreateDTO("Welcome", "en_US");
		Translation translation = translationService.createTranslation(translationCreateDTO, message.getId());
		MessageForTranslator messageForTranslator = new MessageForTranslator();
		messageForTranslator.setId(message.getId());
		messageForTranslator.setTranslation(mapper.map(translation, TranslationForTranslator.class));

		messageDTO.setContent("Witamy serdecznie");
		messageService.updateMessage(messageDTO, message.getId());

		messageDTO.setContent("Witam użytkownika");
		messageService.updateMessage(messageDTO, message.getId());

		// when
		messageForTranslator.setPreviousMessageContent(messageForTranslatorService.getPreviousMessageContent(messageForTranslator));

		// then
		assertEquals("Witam", messageForTranslator.getPreviousMessageContent());
	}
}
