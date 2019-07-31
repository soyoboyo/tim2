package org.tim.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.tim.configuration.SpringJpaTestsCustomExtension;
import org.tim.entities.Message;
import org.tim.entities.Translation;

import java.util.Optional;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.junit.jupiter.api.Assertions.*;

public class TranslationRepositoryTestsIT extends SpringJpaTestsCustomExtension {

	@Autowired
	private TranslationRepository translationRepository;
	@Autowired
	private MessageRepository messageRepository;

	@Test
	@DisplayName("Check if finding translation is working correct.")
	public void testTranslationRepositoryFind() {
		//given
		Translation t = random(Translation.class);
		t.setMessage(messageRepository.save(random(Message.class)));
		Translation translation = translationRepository.save(t);
		//when
		Optional<Translation> actualTranslation = translationRepository.findById(translation.getId());
		//then
		assertAll(
				() -> assertTrue(actualTranslation.isPresent()),
				() -> assertEquals(translation, actualTranslation.get())
		);
	}

	@Test
	@DisplayName("Check if updating translation is working correct.")
	public void testTranslationRepositoryUpdate() {
		//given
		Translation t = random(Translation.class);
		t.setMessage(messageRepository.save(random(Message.class)));
		Translation translation = translationRepository.save(t);
		translation.setContent("Hello.");
		translationRepository.save(translation);
		//when
		Optional<Translation> actualTranslation = translationRepository.findById(translation.getId());
		//then
		assertAll(
				() -> assertTrue(actualTranslation.isPresent()),
				() -> assertEquals(translation, actualTranslation.get())
		);
	}

	@Test
	@DisplayName("Check if deleting translation is working correct.")
	public void testTranslationRepositoryDelete() {
		//given
		Translation t = random(Translation.class);
		t.setMessage(messageRepository.save(random(Message.class)));
		Translation translation = translationRepository.save(t);
		translationRepository.delete(translation);
		//when
		Optional<Translation> actualTranslation = translationRepository.findById(translation.getId());
		//then
		assertAll(
				() -> assertFalse(actualTranslation.isPresent())
		);
	}

	@Test
	@DisplayName("Check translation repository is working correct.")
	void testTranslationRepository() {
		//given
		Translation t = random(Translation.class);
		t.setMessage(messageRepository.save(random(Message.class)));
		Translation translation = translationRepository.save(t);
		//when
		Optional<Translation> actualTranslation = translationRepository.findTranslationsByLocaleAndMessage(translation.getLocale(), translation.getMessage());
		//then
		assertAll(
				() -> assertTrue(actualTranslation.isPresent()),
				() -> assertEquals(translation, actualTranslation.get())
		);
	}
}
