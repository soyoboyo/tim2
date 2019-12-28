package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tim.DTOs.output.LocaleResponse;
import org.tim.configurations.Done;
import org.tim.entities.Message;
import org.tim.entities.Project;
import org.tim.exceptions.EntityNotFoundException;
import org.tim.repositories.MessageRepository;
import org.tim.repositories.ProjectRepository;
import org.tim.repositories.TranslationRepository;
import org.tim.translators.LocaleTranslator;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CIExportsService {

	private final MessageRepository messageRepository;
	private final TranslationRepository translationRepository;
	private final ProjectRepository projectRepository;
	private final LocaleTranslator localeTranslator;

	public String exportAllReadyTranslationsByProjectAndByLocale(String projectId, String locale) {
		Locale orderedLocale = localeTranslator.execute(locale);

		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new EntityNotFoundException("project"));

		List<Message> messages = messageRepository.findActiveMessagesByProject(projectId);

		Map<Locale, Locale> replaceableLocaleToItsSubstitute = project.getReplaceableLocaleToItsSubstitute();


//		List<Translation> translations = translationRepository.findTranslationsByLocaleAndProjectId(orderedLocale, projectId);
//		if (translations.isEmpty()) {
//			for (Map.Entry<LocaleWrapper, LocaleWrapper> entry : replaceableLocaleToItsSubstitute.entrySet()) {
//				if (entry.getKey().getLocale().equals(orderedLocale))
//					translations = translationRepository.findTranslationsByLocaleAndProjectId(
//							entry.getValue().getLocale(), projectId);
//			}
//		}
//		return getAllAvailableTranslationToMessage(translations, messages, orderedLocale);
		return null;
	}

	@Done
	public List<LocaleResponse> getAllSupportedLocalesInProject(String projectId) {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new EntityNotFoundException("project"));

		return project.getTargetLocales()
				.parallelStream()
				.map(locale -> new LocaleResponse(
						locale.toString(),
						locale.getDisplayLanguage(),
						locale.getDisplayCountry()))
				.collect(Collectors.toList());
	}

//	private String getAllAvailableTranslationToMessage(List<Translation> translations,
//													   List<Message> messages,
//													   Locale locale) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("#Messages for locale: " + locale + "\n");
//		sb.append("#" + java.time.LocalDate.now() + "\n");
//
//		Map<String, Message> existingMessages = new HashMap<>();
//		for (Message m : messages) {
//			existingMessages.put(m.getId(), m);
//		}
//		for (Translation t : translations) {
//			if (existingMessages.containsKey(t.getMessage().getId())) {
//				String value = "";
//				if (t.getMessage().isTranslationOutdated(t)) {
//					value = t.getMessage().getContent();
//				} else {
//					value = t.getContent();
//				}
//				sb.append(t.getMessage().getKey() + "=" + value + "\n");
//				existingMessages.remove(t.getMessage().getId());
//			}
//		}
//		for (Message m : existingMessages.values()) {
//			sb.append(m.getKey() + "=" + m.getContent() + "\n");
//		}
//		return sb.toString();
//	}
}
