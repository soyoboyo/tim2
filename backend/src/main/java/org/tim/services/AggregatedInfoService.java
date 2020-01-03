package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tim.DTOs.output.AggregatedInfoForDeveloper;
import org.tim.DTOs.output.AggregatedLocale;
import org.tim.entities.Message;
import org.tim.entities.Project;
import org.tim.entities.Translation;
import org.tim.exceptions.EntityNotFoundException;
import org.tim.repositories.MessageRepository;
import org.tim.repositories.ProjectRepository;
import org.tim.repositories.TranslationRepository;
import org.tim.utils.Pages;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AggregatedInfoService {

	private final ProjectRepository projectRepository;
	private final MessageRepository messageRepository;
	private final TranslationRepository translationRepository;

	private final String CORRECT = "correct";
	private final String INCORRECT = "incorrect";
	private final String MISSING = "missing";

	public AggregatedInfoForDeveloper getAggregatedInfoForDeveloper(String projectId) {
		Project project = getAndValidateProject(projectId);

		List<Message> messages = messageRepository.findActiveMessagesByProject(projectId);
		Set<String> messagesIds = mapMessagesToIds(messages);

		List<Translation> translations = translationRepository.findAllByProjectIdAndMessageIdIn(projectId, messagesIds, Pages.all());
		Map<String, List<Translation>> linkedTranslations = linkTranslationsWithMessageId(translations);

		AggregatedInfoForDeveloper aggregatedInfo = new AggregatedInfoForDeveloper();
		aggregatedInfo.setProjectId(projectId);
		aggregatedInfo.setMessagesTotal(messages.size());

		Map<Locale, Map<String, Integer>> translationStatusesByLocale = prepareAggregatedLocales(project, messages, linkedTranslations);
		aggregatedInfo.setAggregatedLocales(parseData(translationStatusesByLocale));

		return aggregatedInfo;
	}

	private Project getAndValidateProject(String projectId) {
		return projectRepository.findById(projectId)
				.orElseThrow(() -> new EntityNotFoundException("project"));
	}

	private Set<String> mapMessagesToIds(List<Message> messages) {
		return messages
				.stream()
				.map(Message::getId)
				.collect(Collectors.toSet());
	}

	private Map<String, List<Translation>> linkTranslationsWithMessageId(List<Translation> translations) {
		Map<String, List<Translation>> result = new HashMap<>();
		translations.stream()
				.forEach(translation -> {
					if (result.get(translation.getMessageId()) == null) {
						result.put(translation.getMessageId(), new LinkedList<>(Arrays.asList(translation)));
					} else {
						result.get(translation.getMessageId()).add(translation);
					}
				});
		return result;
	}

	private Map<Locale, Map<String, Integer>> prepareAggregatedLocales(
			Project project, List<Message> messages, Map<String, List<Translation>> linkedTranslations) {

		Map<Locale, Map<String, Integer>> translationStatusesByLocale = initEmptyLocaleMaps(project);
		messages.stream().forEach(
				message -> countMissingTranslationsToMessage(
						project,
						message,
						Optional.ofNullable(linkedTranslations.get(message.getId())).orElse(Collections.emptyList()),
						translationStatusesByLocale));

		return translationStatusesByLocale;
	}

	private Map<Locale, Map<String, Integer>> initEmptyLocaleMaps(Project project) {
		Map<Locale, Map<String, Integer>> translationStatusesByLocale = new HashMap<>();
		project.getTargetLocales().stream().forEach(targetLocale -> {
			Map<String, Integer> details = new HashMap<>();
			details.put(CORRECT, 0);
			details.put(INCORRECT, 0);
			details.put(MISSING, 0);
			translationStatusesByLocale.put(targetLocale, details);
		});
		return translationStatusesByLocale;
	}

	private void countMissingTranslationsToMessage(
			Project project, Message message, List<Translation> translations, Map<Locale, Map<String, Integer>> translationStatusesByLocale) {

		Map<Locale, Translation> translationsLinkedWithLocale = mapTranslationToLocale(translations);

		project.getTargetLocales().forEach(
				locale -> {
					String operation = getTranslationStatus(locale, message, translationsLinkedWithLocale);
					translationStatusesByLocale.get(locale).put(
							operation,
							translationStatusesByLocale.get(locale).get(operation) + 1
					);
				});
	}

	private Map<Locale, Translation> mapTranslationToLocale(List<Translation> translations) {
		return translations
				.stream()
				.collect(Collectors.toMap(Translation::getLocale, t -> t));
	}

	private String getTranslationStatus(Locale locale, Message message, Map<Locale, Translation> translationsLinkedWithLocale) {
		Translation translation = translationsLinkedWithLocale.get(locale);
		if (translation == null) return MISSING;
		if (message.isTranslationOutdated(translation) || !translation.getIsValid()) return INCORRECT;
		else return CORRECT;
	}

	private List<AggregatedLocale> parseData(Map<Locale, Map<String, Integer>> translationStatusesByLocale) {
		List<AggregatedLocale> aggregatedLocales = new ArrayList<>();
		for (Map.Entry<Locale, Map<String, Integer>> locale : translationStatusesByLocale.entrySet()) {
			aggregatedLocales.add(new AggregatedLocale(
					locale.getKey().toString(),
					locale.getValue().get(CORRECT),
					locale.getValue().get(INCORRECT),
					locale.getValue().get(MISSING)));
		}
		aggregatedLocales.sort((o1, o2) -> o2.getCorrect().compareTo(o1.getCorrect()));
		return aggregatedLocales;
	}

}
