package org.tim.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tim.configuration.SpringJpaTestsCustomExtension;
import org.tim.entities.TranslationAgency;

import java.util.Optional;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.junit.jupiter.api.Assertions.*;

public class TranslationAgencyRepositoryTestsIT extends SpringJpaTestsCustomExtension {

	@Autowired
	private TranslationAgencyRepository translationAgencyRepository;

	private TranslationAgency randomTranslationAgency;

	@BeforeEach
	public void setUp() {
		randomTranslationAgency = random(TranslationAgency.class);
		randomTranslationAgency.setEmail(null);
	}

	@Test
	public void testTranslationAgencyRepositoryFind() {
		//given
		TranslationAgency translationAgency = translationAgencyRepository.save(randomTranslationAgency);
		//when
		Optional<TranslationAgency> actualTranslationAgency = translationAgencyRepository.findById(translationAgency.getId());
		//then
		assertAll(
				() -> assertTrue(actualTranslationAgency.isPresent()),
				() -> assertEquals(translationAgency, actualTranslationAgency.get())
		);
	}

	@Test
	public void testTranslationAgencyRepositoryUpdate() {
		//given
		TranslationAgency translationAgency = translationAgencyRepository.save(randomTranslationAgency);
		translationAgency.setName("Awesome agency.");
		translationAgencyRepository.save(translationAgency);
		//when
		Optional<TranslationAgency> actualTranslationAgency = translationAgencyRepository.findById(translationAgency.getId());
		//then
		assertAll(
				() -> assertTrue(actualTranslationAgency.isPresent()),
				() -> assertEquals(translationAgency, actualTranslationAgency.get())
		);
	}

	@Test
	public void testTranslationAgencyRepositoryDelete() {
		//given
		TranslationAgency translationAgency = translationAgencyRepository.save(randomTranslationAgency);
		translationAgencyRepository.delete(translationAgency);
		//when
		Optional<TranslationAgency> actualTranslationAgency = translationAgencyRepository.findById(translationAgency.getId());
		//then
		assertAll(
				() -> assertFalse(actualTranslationAgency.isPresent())
		);
	}
}
