package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.tim.DTOs.input.CreateTranslationRequest;
import org.tim.DTOs.input.UpdateTranslationRequest;
import org.tim.annotations.Done;
import org.tim.annotations.ToDo;
import org.tim.entities.Message;
import org.tim.entities.Project;
import org.tim.entities.Translation;
import org.tim.entities.TranslationVersion;
import org.tim.exceptions.EntityAlreadyExistException;
import org.tim.exceptions.EntityNotFoundException;
import org.tim.exceptions.ValidationException;
import org.tim.repositories.MessageRepository;
import org.tim.repositories.ProjectRepository;
import org.tim.repositories.TranslationRepository;
import org.tim.repositories.TranslationVersionRepository;
import org.tim.translators.LocaleTranslator;

import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.tim.utils.UserMessages.LANG_NOT_FOUND_IN_PROJ;

@Service
@RequiredArgsConstructor
public class TranslationService {

	private final TranslationRepository translationRepository;
	private final TranslationVersionRepository translationVersionRepository;
	private final MessageRepository messageRepository;
	private final ProjectRepository projectRepository;
	private final LocaleTranslator localeTranslator;

	private final ModelMapper mapper = new ModelMapper();

	@Done
	@ToDo("Who created this translation")
	public Translation createTranslation(CreateTranslationRequest translationRequest, String messageId) {
		Locale translatedLocale = localeTranslator.execute(translationRequest.getLocale());

		validateIfTranslationNotExists(translatedLocale, messageId);

		Message message = getAndValidateMessage(messageId);
		Project project = getAndValidateProject(message.getProjectId());

		if (project.getTargetLocales()
				.parallelStream()
				.noneMatch(locale -> locale.equals(translatedLocale))) {
			throw new ValidationException(LANG_NOT_FOUND_IN_PROJ
					+ ". The project supports only "
					+ project.getTargetLocales()
					.stream()
					.map(it -> it.toString())
					.collect(Collectors.joining(", "))
					+ " whereas translation is assigned to "
					+ translationRequest.getLocale());
		}
		Translation translation = new Translation();
		translation.setContent(translationRequest.getContent());
		translation.setMessageId(messageId);
		translation.setLocale(translatedLocale);
		translation.setProjectId(message.getProjectId());
		//translation.setCreatedBy();
		return translationRepository.save(translation);
	}

	private void validateIfTranslationNotExists(Locale translatedLocale, String messageId) {
		if (translationRepository.findTranslationByLocaleAndMessageId(translatedLocale, messageId).isPresent()) {
			throw new EntityAlreadyExistException("translation");
		}
	}

	private Project getAndValidateProject(String projectId) {
		return projectRepository.findById(projectId)
				.orElseThrow(() -> new EntityNotFoundException("project"));
	}

	@Done
	public Translation updateTranslation(UpdateTranslationRequest translationRequest, String translationId, String messageId) {
		Translation translation = getAndValidateTranslation(translationId);

		Message message = getAndValidateMessage(messageId);
		saveTranslationVersion(translation);

		translation.setContent(translationRequest.getContent());
		translation.setIsValid(true);
		translation.setUpdateDate(new Date());
		translation.setProjectId(message.getProjectId());
		//translation.setCreatedBy();

		return translationRepository.save(translation);
	}

	public Translation invalidateTranslation(String translationId, String messageId) {
		Translation translation = getAndValidateTranslation(translationId);
		getAndValidateMessage(messageId);
//		TODO LOL
//		if (translation.getMessageId() != messageId) {
//			throw new ValidationException("Inconsistent data");
//		}

		saveTranslationVersion(translation);

		translation.setIsValid(false);
		return translationRepository.save(translation);
	}

	private Message getAndValidateMessage(String messageId) {
		return messageRepository.findById(messageId).orElseThrow(() ->
				new EntityNotFoundException("message"));
	}

	private void saveTranslationVersion(Translation translation) {
		TranslationVersion translationVersion = mapper.map(translation, TranslationVersion.class);
		translationVersion.setTranslationId(translation.getId());
		translationVersionRepository.save(translationVersion);
	}

	private Translation getAndValidateTranslation(String translationId) {
		return translationRepository.findById(translationId).orElseThrow(() ->
				new EntityNotFoundException("translation"));
	}

}
