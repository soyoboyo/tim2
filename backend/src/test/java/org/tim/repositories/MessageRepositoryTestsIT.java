package org.tim.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tim.configuration.SpringJpaTestsCustomExtension;
import org.tim.entities.Message;

import java.util.Optional;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.junit.jupiter.api.Assertions.*;

public class MessageRepositoryTestsIT extends SpringJpaTestsCustomExtension {

	@Autowired
	private MessageRepository messageRepository;

	@Test
	@DisplayName("Check if finding message is working correct.")
	public void testMessageRepositoryFind() {
		//given
		Message message = messageRepository.save(random(Message.class));
		//when
		Optional<Message> actualMessage = messageRepository.findById(message.getId());
		//then
		assertAll(
				() -> assertTrue(actualMessage.isPresent()),
				() -> assertEquals(message, actualMessage.get())
		);
	}

	@Test
	@DisplayName("Check if updating message is working correct.")
	public void testMessageRepositoryUpdate() {
		//given
		Message message = messageRepository.save(random(Message.class));
		message.setContent("Hello");
		messageRepository.save(message);
		//when
		Optional<Message> actualMessage = messageRepository.findById(message.getId());
		//then
		assertAll(
				() -> assertTrue(actualMessage.isPresent()),
				() -> assertEquals(message, actualMessage.get())
		);
	}

	@Test
	@DisplayName("Check if deleting message is working correct.")
	public void testMessageRepositoryDelete() {
		//given
		Message message = messageRepository.save(random(Message.class));
		messageRepository.delete(message);
		//when
		Optional<Message> actualMessage = messageRepository.findById(message.getId());
		//then
		assertAll(
				() -> assertFalse(actualMessage.isPresent())
		);
	}
}
