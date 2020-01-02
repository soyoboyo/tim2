package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tim.DTOs.output.AggregatedInfoForDeveloper;
import org.tim.DTOs.output.AggregatedLocale;
import org.tim.DTOs.output.LocaleResponse;
import org.tim.entities.Message;
import org.tim.entities.Project;
import org.tim.entities.Translation;
import org.tim.exceptions.EntityNotFoundException;
import org.tim.repositories.MessageRepository;
import org.tim.repositories.ProjectRepository;
import org.tim.repositories.TranslationRepository;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AggregatedInfoService {

	private final ProjectRepository projectRepository;
	private final MessageRepository messageRepository;
	private final TranslationRepository translationRepository;

	private final String correct = "correct";
	private final String incorrect = "incorrect";
	private final String missing = "missing";

	public AggregatedInfoForDeveloper getAggregatedInfoForDeveloper(String projectId) {
		AggregatedInfoForDeveloper aggregatedInfo = new AggregatedInfoForDeveloper();
		aggregatedInfo.setProjectId(projectId);

		Project project = getAndValidateProject(projectId);

		Map<Locale, Map<String, Integer>> translationStatusesByLocale = initEmptyLocaleMaps(project );

		// get all messages for project
		List<Message> messages = new LinkedList<>(messageRepository.findActiveMessagesByProject(projectId));

		// iterate over all messages
		for (Message m : messages) {

			// get all translations for message
			List<Translation> translations = translationRepository.findTranslationsByMessageId(m.getId());

			// all translations for each locale are considered missing, unless proven otherwise
			Set<Locale> missingLocales = new HashSet<>(project.getTargetLocales());

			// for each translation check if it correct or (outdated or invalid)
			// then remove it from missing, because it exists
			for (Translation t : translations) {
				Locale locale = t.getLocale();
				updateValuesOfCorrectOrIncorrectTranslations(m, t, locale, translationStatusesByLocale);
				missingLocales.remove(locale);
			}
			// update "missing" value for translations that don't exist
			updateValuesOfMissingTranslations(missingLocales, translationStatusesByLocale);

		}

		aggregatedInfo.setMessagesTotal(messages.size());

		for (Map.Entry<Locale, Map<String, Integer>> locale : translationStatusesByLocale.entrySet()) {
			if (messages.size() > 0)
				locale.getValue().put("coverage", locale.getValue().get("correct") * 100 / messages.size());
			else
				locale.getValue().put("coverage", 100);
		}
		aggregatedInfo.setAggregatedLocales(parseData(translationStatusesByLocale));
		return aggregatedInfo;
	}

	private List<AggregatedLocale> parseData(Map<Locale, Map<String, Integer>> translationStatusesByLocale) {
		List<AggregatedLocale> aggregatedLocales = new ArrayList<>();
		for (Map.Entry<Locale, Map<String, Integer>> locale : translationStatusesByLocale.entrySet()) {
			aggregatedLocales.add(new AggregatedLocale(
					locale.getKey().toString(),
					locale.getValue().get(correct),
					locale.getValue().get(incorrect),
					locale.getValue().get(missing)));
		}
		aggregatedLocales.sort((o1, o2) -> o2.getCorrect().compareTo(o1.getCorrect()));
		return aggregatedLocales;
	}

	private void updateValuesOfCorrectOrIncorrectTranslations(
			Message message,
			Translation translation,
			Locale locale, Map<Locale,
			Map<String, Integer>> translationStatusesByLocale) {

		if (message.isTranslationOutdated(translation) || !translation.getIsValid()) {
			translationStatusesByLocale.get(locale).put(
					incorrect,
					(translationStatusesByLocale.get(locale).get(incorrect) + 1)
			);
		} else {
			translationStatusesByLocale.get(locale).put(
					correct,
					(translationStatusesByLocale.get(locale).get(correct) + 1)
			);
		}
	}

	private void updateValuesOfMissingTranslations(Set<Locale> missingLocales, Map<Locale, Map<String, Integer>> translationStatusesByLocale) {
		for (Locale missingLocale : missingLocales) {
			translationStatusesByLocale.get(missingLocale).put(
					missing,
					(translationStatusesByLocale.get(missingLocale).get(missing) + 1)
			);
		}
	}

	private Map<Locale, Map<String, Integer>> initEmptyLocaleMaps(Project project) {
		Map<Locale, Map<String, Integer>> translationStatusesByLocale = new HashMap<>();
		project.getTargetLocales().stream().forEach(targetLocale -> {
			Map<String, Integer> details = new HashMap<>();
			details.put(correct, 0);
			details.put(incorrect, 0);
			details.put(missing, 0);
			translationStatusesByLocale.put(targetLocale, details);
		});

		return translationStatusesByLocale;
	}

	private Project getAndValidateProject(String projectId) {
		return projectRepository.findById(projectId)
				.orElseThrow(() -> new EntityNotFoundException("project"));
	}
}
