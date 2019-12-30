package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tim.DTOs.output.LocaleResponse;
import org.tim.entities.Message;
import org.tim.entities.Project;
import org.tim.entities.Translation;
import org.tim.exceptions.EntityNotFoundException;
import org.tim.exceptions.ValidationException;
import org.tim.repositories.MessageRepository;
import org.tim.repositories.ProjectRepository;
import org.tim.repositories.TranslationRepository;
import org.tim.translators.LocaleTranslator;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CIExportsService {

	private final MessageRepository messageRepository;
	private final TranslationRepository translationRepository;
	private final ProjectRepository projectRepository;
	private final LocaleTranslator localeTranslator;

	public String exportAllReadyTranslationsByProjectAndByLocale(String projectId, String locale) {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new EntityNotFoundException("project"));
		List<Message> messages = messageRepository.findAllByProjectId(projectId);

		Locale orderedLocale = getAndValidateLocale(project, locale);
		Map<String, Message> messagesMappedWithId = mapMessagesWithIds(messages);
		Set<String> massagesWithMissingTranslations = getAllMessagesIds(messages);

		Set<Locale> replaceableLocales = new HashSet<>(Arrays.asList(orderedLocale));
		Map<String, String> messagesWithTranslations = new HashMap<>();

		Map<String, String> result = findTranslationsToMessages(massagesWithMissingTranslations, replaceableLocales, projectId);
		while (result.size() > 0) {
			Set<String> missingTranslations = massagesWithMissingTranslations;
			massagesWithMissingTranslations = new TreeSet<>();

			for (String messageId : missingTranslations) {
				if (result.get(messageId) != null) {
					messagesWithTranslations.put(messagesMappedWithId.get(messageId).getKey(), result.get(messageId));
				} else {
					massagesWithMissingTranslations.add(messageId);
				}
			}

			replaceableLocales = getNexLayerOfReplaceableLocales(project, replaceableLocales);
			if (massagesWithMissingTranslations.isEmpty() || replaceableLocales.isEmpty()) {
				break;
			}

			result = findTranslationsToMessages(massagesWithMissingTranslations, replaceableLocales, projectId);
		}

		StringBuilder sb = buildPropertiesFile(locale, messagesWithTranslations);
		return sb.toString();
	}

	private Locale getAndValidateLocale(Project project, String localeAsStr) {
		Locale locale = localeTranslator.execute(localeAsStr);
		if (project.getTargetLocales().contains(locale)) {
			return locale;
		}
		throw new ValidationException("Given locale not exists in project's target locales");
	}

	private Map<String, Message> mapMessagesWithIds(List<Message> messages) {
		return messages
				.parallelStream()
				.collect(Collectors.toMap(Message::getId, m -> m));
	}

	private Set<String> getAllMessagesIds(List<Message> messages) {
		return messages
				.parallelStream()
				.map(Message::getId)
				.collect(Collectors.toSet());
	}

	private Map<String, String> findTranslationsToMessages(Set<String> messagesIds, Set<Locale> locales, String projectId) {
		List<Translation> translations = translationRepository.findTranslationsByLocaleInAndProjectIdAndMessageIdIn(locales, projectId, messagesIds);
		return translations
				.parallelStream()
				.collect(Collectors.toMap(Translation::getMessageId, Translation::getContent));
	}

	private Set<Locale> getNexLayerOfReplaceableLocales(Project project, Set<Locale> substituteLocales) {
		Set<Locale> replaceableLocales = new HashSet<>();
		for (Locale locale : substituteLocales) {
			if (project.getSubstituteLocale(locale).isPresent()) {
				replaceableLocales.add(project.getSubstituteLocale(locale).get());
			}
		}
		return replaceableLocales;
	}

	private StringBuilder buildPropertiesFile(String locale, Map<String, String> messagesWithTranslations) {
		StringBuilder sb = new StringBuilder();
		sb.append("#Messages for locale: " + locale + "\n");
		sb.append("#" + java.time.LocalDate.now() + "\n");
		for (Map.Entry<String, String> translation : messagesWithTranslations.entrySet()) {
			sb.append(translation.getKey() + "=" + translation.getValue() + "\n");
		}
		return sb;
	}

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

}
