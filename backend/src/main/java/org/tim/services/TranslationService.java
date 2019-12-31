package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.tim.DTOs.input.CreateTranslationRequest;
import org.tim.DTOs.input.UpdateTranslationRequest;
import org.tim.entities.Message;
import org.tim.entities.Project;
import org.tim.entities.Translation;
import org.tim.entities.TranslationHistory;
import org.tim.exceptions.EntityAlreadyExistException;
import org.tim.exceptions.EntityNotFoundException;
import org.tim.exceptions.ValidationException;
import org.tim.repositories.MessageRepository;
import org.tim.repositories.ProjectRepository;
import org.tim.repositories.TranslationHistoryRepository;
import org.tim.repositories.TranslationRepository;
import org.tim.translators.LocaleTranslator;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.tim.utils.UserMessages.LANG_NOT_FOUND_IN_PROJ;

@Service
@RequiredArgsConstructor
public class TranslationService {

	private final TranslationRepository translationRepository;
	private final TranslationHistoryRepository translationHistoryRepository;
	private final MessageRepository messageRepository;
	private final ProjectRepository projectRepository;
	private final LocaleTranslator localeTranslator;

	public Translation createTranslation(CreateTranslationRequest translationRequest, String messageId, String createdBy) {
		Locale translatedLocale = localeTranslator.execute(translationRequest.getLocale());

		validateIfTranslationNotExists(translatedLocale, messageId);

		Message message = getAndValidateMessage(messageId);
		Project project = getAndValidateProject(message.getProjectId());

		validateProjectSupportGivenLocale(project, translatedLocale);

		Translation translation = new Translation();
		translation.setContent(translationRequest.getContent());
		translation.setMessageId(messageId);
		translation.setLocale(translatedLocale);
		translation.setProjectId(message.getProjectId());
		translation.setCreatedBy(createdBy);

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

	private void validateProjectSupportGivenLocale(Project project, Locale givenLocale) {
		if (project.getTargetLocales()
				.parallelStream()
				.noneMatch(locale -> locale.equals(givenLocale))) {

			throw new ValidationException(LANG_NOT_FOUND_IN_PROJ
					+ ". The project supports only " + project.getTargetLocales()
					.stream().map(it -> it.toString()).collect(Collectors.joining(", "))
					+ " whereas translation is assigned to " + givenLocale);
		}
	}

	public Translation updateTranslation(UpdateTranslationRequest translationRequest, String translationId, String updatedBy) {
		Translation translation = getAndValidateTranslation(translationId);

		saveTranslationVersion(translation);

		translation.setContent(translationRequest.getContent());
		translation.setValid(true);
		translation.setUpdateDate(new Date());
		translation.setCreatedBy(updatedBy);

		return translationRepository.save(translation);
	}

	public Translation invalidateTranslation(String translationId, String invalidatedBy) {
		Translation translation = getAndValidateTranslation(translationId);

		saveTranslationVersion(translation);

		translation.setValid(false);
		translation.setCreatedBy(invalidatedBy);
		translation.setUpdateDate(new Date());

		return translationRepository.save(translation);
	}

	private Message getAndValidateMessage(String messageId) {
		return messageRepository.findById(messageId).orElseThrow(() ->
				new EntityNotFoundException("message"));
	}

	private void saveTranslationVersion(Translation translation) {
		ModelMapper mapper = new ModelMapper();
		TranslationHistory translationHistory = mapper.map(translation, TranslationHistory.class);
		translationHistory.setTranslationId(translation.getId());
		translationHistoryRepository.save(translationHistory);
	}

	private Translation getAndValidateTranslation(String translationId) {
		return translationRepository.findById(translationId).orElseThrow(() ->
				new EntityNotFoundException("translation"));
	}

	public List<TranslationHistory> getTranslationHistoryByParent(String parentId) {
		return translationHistoryRepository.findAllByTranslationIdOrderByUpdateDateDesc(parentId);
	}

}
