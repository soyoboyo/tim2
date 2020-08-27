package org.tim.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tim.DTOs.output.MessageForDeveloper;
import org.tim.configuration.SpringTestsCustomExtension;
import org.tim.entities.Message;
import org.tim.entities.Project;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
		List<MessageForDeveloper> messages = messageForDeveloperService.getMessagesForDeveloper(project.getId());
		//then
		assertAll(
				() -> assertFalse(messages.isEmpty()),
				() -> assertFalse(messages.get(0).getTranslations().isEmpty())
		);
	}

	@Test
	@DisplayName("As developer get messages with translations that are correctly ordered by updateDate")
	void whenAskingForMessagesWithTranslationsThenTheyAreReturnedInCorrectOrder() throws InterruptedException {
		//given
		Project project = createEmptyGermanToEnglishProject();
		List<Message> messages = new ArrayList<>(createMessagesForTestsWithDelay(project));
		messages.sort(Comparator.comparing(Message::getUpdateDate, Comparator.reverseOrder()));
		//when
		List<MessageForDeveloper> messagesForDeveloper = messageForDeveloperService.getMessagesForDeveloper(project.getId());
		//then
		assertAll(
				() -> assertEquals(messagesForDeveloper.get(0).getKey(), messages.get(0).getKey()),
				() -> assertEquals(messagesForDeveloper.get(1).getKey(), messages.get(1).getKey()),
				() -> assertEquals(messagesForDeveloper.get(2).getKey(), messages.get(2).getKey())
		);
	}

	@Test
	@DisplayName("As developer get messages with translations that are correctly ordered by updateDate")
	void whenAskingForMessagesWithTranslationsThatWereUpdatedThenTheyAreReturnedInCorrectOrder() throws InterruptedException {
		//given
		Project project = createEmptyGermanToEnglishProject();
		List<Message> messages = createMessagesForTestsWithDelay(project);
		updateMessage(messageRepository.findByKey(messages.get(1).getKey()).orElseThrow());

		//when
		List<MessageForDeveloper> messagesForDeveloper = messageForDeveloperService.getMessagesForDeveloper(project.getId());
		//then
		assertAll(
				() -> assertEquals(messagesForDeveloper.get(0).getKey(), messages.get(1).getKey()),
				() -> assertEquals(messagesForDeveloper.get(1).getKey(), messages.get(2).getKey()),
				() -> assertEquals(messagesForDeveloper.get(2).getKey(), messages.get(0).getKey())
		);
	}
}
