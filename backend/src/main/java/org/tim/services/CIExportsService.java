package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tim.DTOs.output.LocaleResponse;
import org.tim.annotations.Done;
import org.tim.entities.Message;
import org.tim.entities.Project;
import org.tim.entities.Translation;
import org.tim.exceptions.EntityNotFoundException;
import org.tim.repositories.MessageRepository;
import org.tim.repositories.ProjectRepository;
import org.tim.repositories.TranslationRepository;
import org.tim.translators.LocaleTranslator;

import java.util.HashMap;
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

	@Done
	public String exportAllReadyTranslationsByProjectAndByLocale(String projectId, String locale) {
		Locale orderedLocale = localeTranslator.execute(locale);

		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new EntityNotFoundException("project"));

		List<Message> messages = messageRepository.findActiveMessagesByProject(projectId);

		Map<Locale, Locale> replaceableLocaleToItsSubstitute = project.getReplaceableLocaleToItsSubstitute();

		List<Translation> translations = translationRepository.findTranslationsByLocaleAndProjectId(orderedLocale, projectId);
		if (translations.isEmpty()) {
			for (Map.Entry<Locale, Locale> entry : replaceableLocaleToItsSubstitute.entrySet()) {
				if (entry.getKey().equals(orderedLocale))
					translations = translationRepository.findTranslationsByLocaleAndProjectId(
							entry.getValue(), projectId);
			}
		}
		return getAllAvailableTranslationToMessage(translations, messages, orderedLocale);
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

	private String getAllAvailableTranslationToMessage(List<Translation> translations,
													   List<Message> messages,
													   Locale locale) {
		StringBuilder sb = new StringBuilder();
		sb.append("#Messages for locale: " + locale + "\n");
		sb.append("#" + java.time.LocalDate.now() + "\n");

		Map<String, Message> existingMessages = new HashMap<>();
		for (Message m : messages) {
			existingMessages.put(m.getId(), m);
		}
		for (Translation t : translations) {

			//TODO pobieranie messagow jest juz wykonane w poprzednim kroku. Do optymalizacji w przyszlosci!
			Message message = messageRepository.findById(t.getMessageId())
					.orElseThrow(() -> new EntityNotFoundException("message"));
			if (existingMessages.containsKey(t.getMessageId())) {
				String value = "";
				if (message.isTranslationOutdated(t)) {
					value = message.getContent();
				} else {
					value = t.getContent();
				}
				sb.append(message.getKey() + "=" + value + "\n");
				existingMessages.remove(message.getId());
			}
		}
		for (Message m : existingMessages.values()) {
			sb.append(m.getKey() + "=" + m.getContent() + "\n");
		}
		return sb.toString();
	}
}
