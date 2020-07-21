package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.LocaleUtils;
import org.springframework.stereotype.Service;
import org.tim.entities.LocaleWrapper;
import org.tim.entities.Message;
import org.tim.entities.Project;
import org.tim.entities.Translation;
import org.tim.exceptions.EntityNotFoundException;
import org.tim.exceptions.ValidationException;
import org.tim.repositories.MessageRepository;
import org.tim.repositories.ProjectRepository;
import org.tim.repositories.TranslationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CDExportsService {

	private final TranslationRepository translationRepository;
	private final ProjectRepository projectRepository;
	private final MessageRepository messageRepository;

	public Map<String, String> exportAllReadyTranslationsByProjectAndByLocale(Long projectId, String locale) {
		Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("project"));
		List<Message> messages = messageRepository.findMessagesByProjectIdAndIsArchivedFalse(projectId);
		Map<LocaleWrapper, LocaleWrapper> replaceableLocaleToItsSubstitute = project.getReplaceableLocaleToItsSubstitute();

		Locale orderedLocale;
		try {
			orderedLocale = LocaleUtils.toLocale(locale);
		} catch (IllegalArgumentException e) {
			throw new ValidationException("Locale: " + locale + " doesn't exist.");
		}

		List<Translation> translations = translationRepository.findTranslationsByLocaleAndProjectId(orderedLocale, projectId);

		if (translations.isEmpty()) {
			for (Map.Entry<LocaleWrapper, LocaleWrapper> entry : replaceableLocaleToItsSubstitute.entrySet()) {
				if (entry.getKey().getLocale().equals(orderedLocale))
					translations = translationRepository.findTranslationsByLocaleAndProjectId(
							entry.getValue().getLocale(), projectId);
			}
		}

		return translationsOrMessagesToMap(translations, messages);
	}

	private Map<String, String> translationsOrMessagesToMap(List<Translation> translations, List<Message> messages) {
		Map<String, String> map = new HashMap<>();
		for (Translation translation : translations) {
			map.put(translation.getMessage().getKey(), translation.getContent());
		}
		for (Message message : messages) {
			map.put(message.getKey(), message.getContent());
		}

		return map;
	}
}
