package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.LocaleUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.tim.DTOs.input.TranslationCreateDTO;
import org.tim.DTOs.input.TranslationUpdateDTO;
import org.tim.entities.Message;
import org.tim.entities.Translation;
import org.tim.entities.TranslationVersion;
import org.tim.exceptions.EntityAlreadyExistException;
import org.tim.exceptions.EntityNotFoundException;
import org.tim.exceptions.ValidationException;
import org.tim.repositories.MessageRepository;
import org.tim.repositories.TranslationRepository;
import org.tim.repositories.TranslationVersionRepository;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import static org.tim.utils.UserMessages.LANG_NOT_FOUND_IN_PROJ;

@Service
@RequiredArgsConstructor
public class TranslationService {

	private final TranslationRepository translationRepository;
	private final TranslationVersionRepository translationVersionRepository;
	private final MessageRepository messageRepository;

	private final ModelMapper mapper = new ModelMapper();

	public Translation createTranslation(TranslationCreateDTO translationCreateDTO, String messageId) {
		checkIfTranslationAlreadyExists(translationCreateDTO.getLocale(), messageId);
		Message message = checkIfMessageExists(messageId);

		Set<Locale> projectTargetLocales = null;
				//message.getProject().getTargetLocales();
		Locale translationLocale = LocaleUtils.toLocale(translationCreateDTO.getLocale());
		boolean existInProject = false;
		for (Locale localeWrapper : projectTargetLocales) {
			if (localeWrapper.equals(translationLocale)) {
				existInProject = true;
			}
		}
		if (!existInProject) {
			throw new ValidationException(LANG_NOT_FOUND_IN_PROJ
					+ ". The project supports only "
					+ projectTargetLocales.stream().map(it -> it.toString()).collect(Collectors.joining(", "))
					+ " whereas translation is assigned to "
					+ translationCreateDTO.getLocale());
		}
		Translation translation = new Translation();
		translation.setContent(translationCreateDTO.getContent());
		translation.setMessage(message);
		translation.setLocale(translationLocale);
		return translationRepository.save(translation);
	}

	public Translation updateTranslation(TranslationUpdateDTO translationUpdateDTO, String translationId, String messageId) {
		Translation translation = checkIfTranslationExists(translationId);
		checkIfMessageExists(messageId);

		saveTranslationVersion(translation);

		translation.setContent(translationUpdateDTO.getContent());
		translation.setIsValid(true);
		return translationRepository.save(translation);
	}

	public Translation invalidateTranslation(String translationId, String messageId) {
		Translation translation = checkIfTranslationExists(translationId);
		checkIfMessageExists(messageId);

		saveTranslationVersion(translation);

		translation.setIsValid(false);
		return translationRepository.save(translation);
	}

	public Translation archiveTranslation(String id) {
		Translation translation = checkIfTranslationExists(id);

		saveTranslationVersion(translation);

		translation.setIsArchived(true);
		return translationRepository.save(translation);
	}

	private void saveTranslationVersion(Translation translation) {
		TranslationVersion translationVersion = mapper.map(translation, TranslationVersion.class);
		translationVersion.setTranslationId(translation.getId());
		translationVersionRepository.save(translationVersion);
	}

	private Translation checkIfTranslationExists(String translationId) {
		return translationRepository.findById(translationId).orElseThrow(() ->
				new EntityNotFoundException("translation"));
	}

	private Message checkIfMessageExists(String messageId) {
		return messageRepository.findById(messageId).orElseThrow(() ->
				new EntityNotFoundException("message"));
	}

	private void checkIfTranslationAlreadyExists(String locale, String messageId) {
		Translation translation = translationRepository.findTranslationByLocaleAndMessageId(LocaleUtils.toLocale(locale), messageId);
		if (translation != null) {
			throw new EntityAlreadyExistException("translation");
		}
	}


}
