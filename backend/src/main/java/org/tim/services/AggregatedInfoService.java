package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tim.DTOs.output.AggregatedInfoForDeveloper;
import org.tim.entities.LocaleWrapper;
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

	String correct = "correct";
	String incorrect = "incorrect";
	String missing = "missing";


	public AggregatedInfoForDeveloper getAggregatedInfoForDeveloper(Long projectId) {
		AggregatedInfoForDeveloper aggregatedInfo = new AggregatedInfoForDeveloper();
		aggregatedInfo.setProjectId(projectId);

		Map<String, Map<String, Integer>> translationStatusesByLocale = new HashMap<>();
		Set<String> targetLocales = initEmptyLocaleMaps(projectId, translationStatusesByLocale);

		// get all messages for project
		List<Message> messages = new LinkedList<>(messageRepository.findMessagesByProjectIdAndIsArchivedFalse(projectId));

		// iterate over all messages
		for (Message m : messages) {

			// get all translations for message
			List<Translation> translations = translationRepository.findTranslationsByMessageIdAndIsArchivedFalse(m.getId());

			// all translations for each locale are considered missing, unless proven otherwise
			Set<String> missingLocales = new HashSet<>(targetLocales);

			// for each translation check if it correct or (outdated or invalid)
			// then remove it from missing, because it exists
			for (Translation t : translations) {
				String locale = t.getLocale().toString();
				updateValuesOfCorrectOrIncorrectTranslations(m, t, locale, translationStatusesByLocale);
				missingLocales.remove(locale);
			}
			// update "missing" value for translations that don't exist
			updateValuesOfMissingTranslations(missingLocales, translationStatusesByLocale);

		}

		aggregatedInfo.setTranslationStatusesByLocale(translationStatusesByLocale);
		aggregatedInfo.setMessagesTotal(messages.size());

		for (Map.Entry<String, Map<String, Integer>> locale : translationStatusesByLocale.entrySet()) {
			locale.getValue().put("coverage", locale.getValue().get("correct") * 100 / messages.size());
		}

		return aggregatedInfo;
	}

	private void updateValuesOfCorrectOrIncorrectTranslations(
			Message message,
			Translation translation,
			String locale, Map<String,
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

	private void updateValuesOfMissingTranslations(Set<String> missingLocales, Map<String, Map<String, Integer>> translationStatusesByLocale) {
		for (String missingLocale : missingLocales) {
			translationStatusesByLocale.get(missingLocale).put(
					missing,
					(translationStatusesByLocale.get(missingLocale).get(missing) + 1)
			);
		}
	}

	private Set<String> initEmptyLocaleMaps(Long projectId, Map<String, Map<String, Integer>> translationStatusesByLocale) {
		Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("project"));
		Set<String> targetLocales = new HashSet<>();
		for (LocaleWrapper lw : project.getTargetLocales()) {
			Map<String, Integer> details = new HashMap<>();
			details.put(correct, 0);
			details.put(incorrect, 0);
			details.put(missing, 0);
			translationStatusesByLocale.put(lw.getLocale().toString(), details);
			targetLocales.add(lw.getLocale().toString());
		}
		return targetLocales;
	}
}
