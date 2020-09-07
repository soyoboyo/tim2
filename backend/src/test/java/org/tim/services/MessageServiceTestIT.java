package org.tim.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tim.DTOs.MessageDTO;
import org.tim.DTOs.input.TranslationCreateDTO;
import org.tim.DTOs.input.TranslationUpdateDTO;
import org.tim.configuration.SpringTestsCustomExtension;
import org.tim.entities.Message;
import org.tim.entities.MessageVersion;
import org.tim.entities.Project;
import org.tim.entities.Translation;
import org.tim.repositories.MessageRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.junit.jupiter.api.Assertions.*;

public class MessageServiceTestIT extends SpringTestsCustomExtension {

	private Project project;

	@Autowired
	private MessageService messageService;

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private MessageVersionService messageVersionService;

	@Autowired
	private TranslationService translationService;

	@BeforeEach
	void setUp() {
		project = createEmptyGermanToEnglishProject();
	}

	@Test
	@DisplayName("Create message when data were sent in correct form.")
	void whenValidMessageIsGivingThenMessageIsCreated() {
		//given
		MessageDTO messageDTO = random(MessageDTO.class);
		messageDTO.setProjectId(project.getId());
		//when
		Message createdMessage = messageService.createMessage(messageDTO);
		Optional<Message> messageFromDB = messageRepository.findById(createdMessage.getId());
		//then
		assertTrue(messageFromDB.isPresent());
		assertEquals(messageDTO.getContent(), messageFromDB.get().getContent());
	}

	@Test
	@DisplayName("Throws exception when message data were sent in wrong format.")
	void whenInvalidMessageIsGivingThenExceptionIsThrown() {
		//given
		MessageDTO messageDTO = random(MessageDTO.class);
		//when
		//then
		assertThrows(NoSuchElementException.class, () -> messageService.createMessage(messageDTO));
	}

	@Test
	@DisplayName("Assert messages are deleted correctly.")
	void whenDeletingExistingMessageThenMessageIsDeleted() {
		//given
		MessageDTO messageDTO = random(MessageDTO.class);
		messageDTO.setProjectId(project.getId());
		Message createdMessage = messageService.createMessage(messageDTO);
		//when
		Message message = messageService.archiveMessage(createdMessage.getId());
		Optional<Message> messageFromDB = messageRepository.findById(message.getId());
		//then
		assertTrue(messageFromDB.get().getIsArchived());
	}

	@Test
	@DisplayName("Throws exception when try to delete not existed messages.")
	void whenDeletingNonExistingMessageThenExceptionIsThrown() {
		//given
		Long id = 1L;
		//when
		//then
		assertThrows(NoSuchElementException.class, () -> messageService.archiveMessage(id));
	}

	@Test
	@DisplayName("Assert message updated correctly.")
	void whenUpdateExistingNotDeletedMessageThenMessageIsUpdated() {
		//given
		MessageDTO messageDTO = random(MessageDTO.class);
		messageDTO.setProjectId(project.getId());
		Message createdMessage = messageService.createMessage(messageDTO);
		messageDTO.setContent("updated_content");
		//when
		Message updatedMessage = messageService.updateMessage(messageDTO, createdMessage.getId());
		Optional<Message> messageFromDB = messageRepository.findById(updatedMessage.getId());
		//then
		assertEquals("updated_content", messageFromDB.get().getContent());
	}

	@Test
	@DisplayName("Throws exception when user try to update deleted messages.")
	void whenUpdateExistingDeletedMessageThenExceptionIsThrown() {
		//given
		MessageDTO messageDTO = random(MessageDTO.class);
		messageDTO.setProjectId(project.getId());
		Message createdMessage = messageService.createMessage(messageDTO);
		messageDTO.setContent("updated_content");
		messageService.archiveMessage(createdMessage.getId());
		//when
		//then
		assertThrows(NoSuchElementException.class, () -> messageService.updateMessage(messageDTO, createdMessage.getId()));
	}

	@Test
	@DisplayName("When user try update not existed messages then throw exception.")
	void whenUpdateNonExistingNotDeletedMessageThenExceptionIsThrown() {
		//given
		MessageDTO messageDTO = random(MessageDTO.class);
		messageDTO.setProjectId(project.getId());
		Long id = 1L;
		//when
		//then
		assertThrows(NoSuchElementException.class, () -> messageService.updateMessage(messageDTO, id));
	}

	@Test
	@DisplayName("Assert that new message version is created when message is updated.")
	void whenMessageIsUpdatedThenMessageVersionIsCreated() throws InterruptedException {
		// given
		Project project = createEmptyGermanToEnglishProject();
		MessageDTO messageDTO = random(MessageDTO.class);
		messageDTO.setProjectId(project.getId());
		messageDTO.setKey("key 0");
		messageDTO.setContent("content version 0");
		Message message = messageService.createMessage(messageDTO);

		messageDTO.setKey("key 1");
		messageDTO.setContent("content version 1");
		Thread.sleep(1);
		messageService.updateMessage(messageDTO, message.getId());
		messageDTO.setKey("key 2");
		messageDTO.setContent("content version 2");

		// when
		Message updatedMessage = messageService.updateMessage(messageDTO, message.getId());
		List<MessageVersion> messageVersions = messageVersionService.getMessageVersionsByOriginalId(message.getId());
		// then
		assertAll(
				() -> assertEquals(messageDTO.getContent(), updatedMessage.getContent()),
				() -> assertFalse(messageVersions.isEmpty()),
				() -> assertEquals(2, messageVersions.size()),
				() -> assertEquals("content version 1", messageVersions.get(0).getContent()),
				() -> assertEquals("content version 0", messageVersions.get(1).getContent())
		);
	}

	@Test
	@DisplayName("Check when deleting message, there is also removed message history, translations and translations history")
	void whenMessageIsDeleted_thenAllChildRelationsObjectsAreAlsoRemoved() {
		// given
		Project project = createEmptyGermanToEnglishAndFrenchProject();
		MessageDTO messageDTO = random(MessageDTO.class);
		messageDTO.setProjectId(project.getId());
		// create message
		Message message = messageService.createMessage(messageDTO);
		// update message
		messageDTO.setContent("Updated content");
		Message updatedMessage = messageService.updateMessage(messageDTO, message.getId());
		// create translation for lang 1
		TranslationCreateDTO tdto1 = new TranslationCreateDTO("Good morning", "en");
		Translation t1 = translationService.createTranslation(tdto1, message.getId());
		// create translation for lang 2
		TranslationCreateDTO tdto2 = new TranslationCreateDTO("Bonjour", "fr");
		Translation t2 = translationService.createTranslation(tdto2, message.getId());
		// update translation 1
		TranslationUpdateDTO tupdto1 = new TranslationUpdateDTO("updated t1");
		translationService.updateTranslation(tupdto1, t1.getId(), message.getId());
		// update translation 2
		TranslationUpdateDTO tupdto2 = new TranslationUpdateDTO("updated t1");
		translationService.updateTranslation(tupdto2, t2.getId(), message.getId());
		// when
		// delete message
		messageService.deleteMessageAndTranslations(message.getId());

		// then
		// all objects should not be present in database

		long messageId = message.getId();
		assertAll(
				() -> assertTrue(messageRepository.findById(messageId).isEmpty()),
				() -> assertTrue(messageVersionRepository.findAllByMessageId(messageId).isEmpty()),
				() -> assertTrue(translationRepository.findAllByMessageId(messageId).isEmpty()),
				() -> assertTrue(translationVersionRepository.findAllByTranslationId(t1.getId()).isEmpty()),
				() -> assertTrue(translationVersionRepository.findAllByTranslationId(t2.getId()).isEmpty())

		);

	}
}
