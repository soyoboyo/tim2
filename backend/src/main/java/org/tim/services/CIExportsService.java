package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.LocaleUtils;
import org.springframework.stereotype.Service;
import org.tim.DTOs.output.LocaleDTO;
import org.tim.entities.LocaleWrapper;
import org.tim.entities.Message;
import org.tim.entities.Project;
import org.tim.entities.Translation;
import org.tim.exceptions.EntityNotFoundException;
import org.tim.exceptions.ValidationException;
import org.tim.repositories.MessageRepository;
import org.tim.repositories.ProjectRepository;
import org.tim.repositories.TranslationRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CIExportsService {

	private final MessageRepository messageRepository;
	private final TranslationRepository translationRepository;
	private final ProjectRepository projectRepository;

	public String exportAllReadyTranslationsByProjectAndByLocale(Long projectId, String locale) {
		Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("project"));
		List<Message> messages = messageRepository.findMessagesByProjectIdAndIsRemovedFalse(projectId);
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
		return getAllAvailableTranslationToMessage(translations, messages, orderedLocale);
	}

	public List<LocaleDTO> getAllSupportedLocalesInProject(Long projectId) {
		Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("project"));
		List<LocaleDTO> supportedLocales = new LinkedList<>();
		for (LocaleWrapper localeWrapper : project.getTargetLocales()) {
			supportedLocales.add(new LocaleDTO(localeWrapper.getLocale().toString(),
					localeWrapper.getLocale().getDisplayLanguage(),
					localeWrapper.getLocale().getDisplayCountry()));
		}
		return supportedLocales;
	}

	private String getAllAvailableTranslationToMessage(List<Translation> translations,
													   List<Message> messages,
													   Locale locale) {
		StringBuilder sb = new StringBuilder();
		sb.append("#Messages for locale: " + locale + "\n");
		sb.append("#" + java.time.LocalDate.now() + "\n");
		Map<Long, Message> existingMessages = new HashMap<>();
		for (Message m : messages) {
			existingMessages.put(m.getId(), m);
		}
		for (Translation t : translations) {
			if (existingMessages.containsKey(t.getMessage().getId())) {
				String value = "";
				if (t.getMessage().isTranslationOutdated(t)) {
					value = t.getMessage().getContent();
				} else {
					value = t.getContent();
				}
				sb.append(t.getMessage().getKey() + "=" + value + "\n");
				existingMessages.remove(t.getMessage().getId());
			}
		}
		for (Message m : existingMessages.values()) {
			sb.append(m.getKey() + "=" + m.getContent() + "\n");
		}
		return sb.toString();
	}
}
