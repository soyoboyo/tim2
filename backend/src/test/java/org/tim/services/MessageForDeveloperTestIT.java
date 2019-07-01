package org.tim.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.tim.DTOs.output.MessageForDeveloper;
import org.tim.configuration.SpringTestsCustomExtension;
import org.tim.entities.Project;

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
	@WithMockUser(username = "prog", password = "prog")
	void whenAskingForMessagesWithTranslationsThenTheyAreReturned() {
		//giving
		createTenRandomMessages(project);
		createTranslationsForMessages();
		//when
		List<MessageForDeveloper> messages = messageForDeveloperService.getMessagesForDeveloper(project.getId());
		//then
		assertAll(
				() -> assertFalse(messages.isEmpty()),
				() -> assertEquals(getMessagesCount(), messages.size()),
				() -> assertFalse(messages.get(0).getTranslations().isEmpty())
		);
	}
}
