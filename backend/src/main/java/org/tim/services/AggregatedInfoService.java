package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tim.entities.*;
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

	public AggregatedInfoForDeveloper getAggregatedInfoForDeveloper(Long projectId) {
		AggregatedInfoForDeveloper aggregatedInfo = new AggregatedInfoForDeveloper();
		aggregatedInfo.setProjectId(projectId);

		String correct = "correct";
		String incorrect = "incorrect";
		String missing = "missing";

		Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("project"));

		Map<String, Map<String, Integer>> translationStatusesByLocale = new HashMap<>();
		Set<String> targetLocales = new HashSet<>();
		for (LocaleWrapper lw : project.getTargetLocales()) {
			Map<String, Integer> details = new HashMap<>();
			details.put(correct, 0);
			details.put(incorrect, 0);
			details.put(missing, 0);
			translationStatusesByLocale.put(lw.getLocale().toString(), details);
			targetLocales.add(lw.getLocale().toString());
		}

		List<Message> messages = new LinkedList<>(messageRepository.findMessagesByProjectIdAndIsArchivedFalse(projectId));
		for (Message m : messages) {
			List<Translation> translations = translationRepository.findTranslationsByMessageIdAndIsArchivedFalse(m.getId());
			Set<String> missingLocales = new HashSet<>(targetLocales);

			for (Translation t : translations) {
				String locale = t.getLocale().toString();
				if (m.isTranslationOutdated(t) || !t.getIsValid()) {
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
				missingLocales.remove(locale);
			}

			for (String missingLocale : missingLocales) {
				translationStatusesByLocale.get(missingLocale).put(
						missing,
						(translationStatusesByLocale.get(missingLocale).get(missing) + 1)
				);
			}
		}

		aggregatedInfo.setTranslationStatusesByLocale(translationStatusesByLocale);
		aggregatedInfo.setMessagesTotal(messages.size());

		for (Map.Entry<String, Map<String, Integer>> locale : translationStatusesByLocale.entrySet()) {
			locale.getValue().put("coverage", (locale.getValue().get(correct) * 100) / (messages.size()));
		}

		return aggregatedInfo;
	}
}
