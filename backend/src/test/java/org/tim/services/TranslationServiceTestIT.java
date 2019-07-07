package org.tim.services;

import org.apache.commons.lang.LocaleUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.tim.DTOs.input.TranslationCreateDTO;
import org.tim.DTOs.input.TranslationUpdateDTO;
import org.tim.configuration.SpringTestsCustomExtension;
import org.tim.entities.*;
import org.tim.exceptions.EntityNotFoundException;
import org.tim.exceptions.ValidationException;
import org.tim.repositories.LocaleWrapperRepository;
import org.tim.repositories.MessageRepository;
import org.tim.repositories.ProjectRepository;
import org.tim.repositories.TranslationRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.junit.jupiter.api.Assertions.*;
import static org.tim.utils.UserMessages.LANG_NOT_FOUND_IN_PROJ;

public class TranslationServiceTestIT extends SpringTestsCustomExtension {

	@Autowired
	private TranslationService translationService;
	@Autowired
	private TranslationVersionService translationVersionService;
	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private LocaleWrapperRepository localeWrapperRepository;
	@Autowired
	private TranslationRepository translationRepository;

	private TranslationCreateDTO translationCreateDTO;
	private Translation expectedTranslation;
	private TranslationUpdateDTO translationUpdateDTO;

	@BeforeEach
	public void setUp() {
		clear();
		expectedTranslation = new Translation();
		expectedTranslation.setContent("Default context");
		expectedTranslation.setLocale(Locale.ENGLISH);

		translationCreateDTO = new TranslationCreateDTO();
		translationCreateDTO.setContent("Default context");
		translationCreateDTO.setLocale("en");

		translationUpdateDTO = new TranslationUpdateDTO();
		translationUpdateDTO.setContent("Default context");

		createRandomMessage(createEmptyGermanToEnglishProject());
	}

	@Test
	@WithMockUser(username = "tran", password = "tran")
	public void createNewTranslation_DataCorrect_Success() {
		Long messageId = messageRepository.findAll().get(0).getId();
		Translation responseTranslation = translationService.createTranslation(translationCreateDTO, messageId);
		assertAll(
				() -> assertEquals(responseTranslation.getContent(), expectedTranslation.getContent()),
				() -> assertEquals(responseTranslation.getLocale(), expectedTranslation.getLocale())
		);
	}

	@Test
	@WithMockUser(username = "tran", password = "tran")
	public void createNewTranslation_TranslationLanguageNotExistInProjectTargetLocales_ThrowException() {
		Long messageId = messageRepository.findAll().get(0).getId();
		translationCreateDTO.setLocale("pl_DE");
		Exception exception = assertThrows(ValidationException.class, () ->
				translationService.createTranslation(translationCreateDTO, messageId));
		assertTrue(exception.getMessage().startsWith(LANG_NOT_FOUND_IN_PROJ));
	}

	@Test
	@WithMockUser(username = "tran", password = "tran")
	public void createNewTranslation_MessageNotExist_ThrowException() {
		Exception exception = assertThrows(EntityNotFoundException.class, () ->
				translationService.createTranslation(translationCreateDTO, 100L));
		assertEquals(exception.getMessage(), "Sorry, we can't find this message");
	}

	@Test
	@WithMockUser(username = "tran", password = "tran")
	public void createMessage_createTranslation__MessageTimeIsBefore() throws InterruptedException {
		Message message = messageRepository.findAll().get(0);
		message.setContent("testContent");
		message = messageRepository.save(message);
		Thread.sleep(10L);
		Translation translation = translationService.createTranslation(translationCreateDTO, message.getId());
		assertTrue(message.getUpdateDate().isBefore(translation.getUpdateDate()));
	}

	@Test
	@WithMockUser(username = "tran", password = "tran")
	public void createTranslation_UpdateMessage__MessageTimeIsAfter() throws InterruptedException {
		Message message = messageRepository.findAll().get(0);
		Translation translation = translationService.createTranslation(translationCreateDTO, message.getId());
		message.setContent("newContext");
		Thread.sleep(10L);
		assertTrue(messageRepository.save(message).getUpdateDate().isAfter(translation.getUpdateDate()));
	}

	@Test
	@WithMockUser(username = "tran", password = "tran")
	public void createNewTranslation__DataCorrectWithExistingDateInDatabase_Success() {
		Project project = new Project();
		project.setName("ProjectForTest");
		LocaleWrapper localeWrapper = localeWrapperRepository.save(new LocaleWrapper(Locale.ENGLISH));
		project.setSourceLocale(LocaleUtils.toLocale("de_DE"));
		project.addTargetLocale(Arrays.asList(localeWrapper));
		project = projectRepository.save(project);
		Message message = new Message();
		message.setProject(project);
		message.setDescription("DescriptionForTest");
		message.setContent("ContentForTest");
		message.setKey("KeyForTest");
		message = messageRepository.save(message);
		Translation responseTranslation = translationService.createTranslation(translationCreateDTO, message.getId());
		assertAll(
				() -> assertEquals(responseTranslation.getContent(), expectedTranslation.getContent()),
				() -> assertEquals(responseTranslation.getLocale(), expectedTranslation.getLocale())
		);
	}

	@Test
	@WithMockUser(username = "tran", password = "tran")
	void whenTranslationIsUpdatedThenUpdatedVersionIsReturned() {
		// given
		Project project = createEmptyGermanToEnglishProject();
		Message message = createRandomMessage(project);
		translationCreateDTO.setLocale("en");
		Translation translation = translationService.createTranslation(translationCreateDTO, message.getId());
		translationUpdateDTO.setContent("new_content");
		// when
		Translation updatedTranslation = translationService.updateTranslation(translationUpdateDTO, translation.getId(), message.getId());
		// then
		assertEquals(translationUpdateDTO.getContent(), updatedTranslation.getContent());
	}

	@Test
	@WithMockUser(username = "tran", password = "tran")
	void whenTranslationIsUpdatedThenTranslationVersionIsCreated() {
		// given
		Project project = createEmptyGermanToEnglishProject();
		Message message = createRandomMessage(project);
		translationCreateDTO.setLocale("en");
		translationCreateDTO.setContent("content version 0");
		Translation translation = translationService.createTranslation(translationCreateDTO, message.getId());
		translationUpdateDTO.setContent("content version 1");
		translationService.updateTranslation(translationUpdateDTO, translation.getId(), message.getId());
		translationUpdateDTO.setContent("content version 2");
		// when
		Translation updatedTranslation = translationService.updateTranslation(translationUpdateDTO, translation.getId(), message.getId());
		List<TranslationVersion> translationVersions = translationVersionService.getTranslationVersionsByOriginal(translation.getId());
		// then
		assertAll(
				() -> assertEquals(translationUpdateDTO.getContent(), updatedTranslation.getContent()),
				() -> assertFalse(translationVersions.isEmpty()),
				() -> assertEquals(2, translationVersions.size()),
				() -> assertEquals("content version 1", translationVersions.get(0).getContent()),
				() -> assertEquals("content version 0", translationVersions.get(1).getContent())
		);
	}


	@Test
	void whenArchivingExistingTranslationThenTranslationIsArchived() {
		//given
		Project project = createEmptyGermanToEnglishProject();
		Message message = createRandomMessage(project);
		TranslationCreateDTO translationCreateDTO = random(TranslationCreateDTO.class);
		translationCreateDTO.setLocale("en");
		Translation createdTranslation = translationService.createTranslation(translationCreateDTO, message.getId());
		//when
		Translation translation = translationService.archiveTranslation(createdTranslation.getId());
		Optional<Translation> translationFromDB = translationRepository.findById(translation.getId());
		//then
		assertTrue(translationFromDB.get().getIsArchived());
	}
}
