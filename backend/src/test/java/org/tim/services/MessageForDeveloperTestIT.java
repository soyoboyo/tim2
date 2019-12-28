package org.tim.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tim.DTOs.output.MessageForDeveloperResponse;
import org.tim.configuration.SpringTestsCustomExtension;
import org.tim.entities.Message;
import org.tim.entities.Project;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

public class MessageForDeveloperTestIT extends SpringTestsCustomExtension {

	private Project project;

	@Autowired
	private MessageForDeveloperService messageForDeveloperService;

	@BeforeEach
	void setUp() {
		project = createEmptyGermanToEnglishProject();
	}

	@Test
	@DisplayName("As developer get messages with translations")
	void whenAskingForMessagesWithTranslationsThenTheyAreReturned() {
		//giving
		createTenRandomMessages(project);
		createTranslationsForMessages();
		//when
		List<MessageForDeveloperResponse> messages = messageForDeveloperService.getMessagesForDeveloper(project.getId());
		//then
		assertAll(
				() -> assertFalse(messages.isEmpty()),
				() -> assertEquals(getMessagesCount(), messages.size()),
				() -> assertFalse(messages.get(0).getTranslations().isEmpty())
		);
	}

	@Test
	@DisplayName("As developer get messages with translations that are correctly ordered by updateDate")
	void whenAskingForMessagesWithTranslationsThenTheyAreReturnedInCorrectOrder() {
		//given
		Project project = createEmptyGermanToEnglishProject();
		List<Message> messages = createMessagesForTests(project);
		//when
		List<MessageForDeveloperResponse> messagesForDeveloper = messageForDeveloperService.getMessagesForDeveloper(project.getId());
		//then
		assertAll(
				() -> assertEquals(messagesForDeveloper.get(0).getKey(), messages.get(2).getKey()),
				() -> assertEquals(messagesForDeveloper.get(1).getKey(), messages.get(1).getKey()),
				() -> assertEquals(messagesForDeveloper.get(2).getKey(), messages.get(0).getKey())
		);
	}

	@Test
	@DisplayName("As developer get messages with translations that are correctly ordered by updateDate")
	void whenAskingForMessagesWithTranslationsThatWereUpdatedThenTheyAreReturnedInCorrectOrder() {
		//given
		Project project = createEmptyGermanToEnglishProject();
		List<Message> messages = createMessagesForTests(project);
		messages.get(1).setContent("New content");
		messageRepository.saveAll(messages);
		//when
		List<MessageForDeveloperResponse> messagesForDeveloper = messageForDeveloperService.getMessagesForDeveloper(project.getId());
		//then
		assertAll(
				() -> assertEquals(messagesForDeveloper.get(0).getKey(), messages.get(1).getKey()),
				() -> assertEquals(messagesForDeveloper.get(1).getKey(), messages.get(2).getKey()),
				() -> assertEquals(messagesForDeveloper.get(2).getKey(), messages.get(0).getKey())
		);
	}

	private List<Message> createMessagesForTests(Project project) {
		List<Message> messages = Arrays.asList(
				new Message("key1", "Content1", project.getId()),
				new Message("key2", "Content2", project.getId()),
				new Message("key3", "Content3", project.getId())
		);
		return StreamSupport
				.stream(messageRepository.saveAll(messages).spliterator(), false)
				.collect(Collectors.toList());
	}
}
