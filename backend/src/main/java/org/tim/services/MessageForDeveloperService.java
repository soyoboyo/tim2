package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tim.DTOs.output.MessageForDeveloper;
import org.tim.DTOs.output.TranslationForDeveloper;
import org.tim.entities.LocaleWrapper;
import org.tim.entities.Message;
import org.tim.entities.Project;
import org.tim.entities.Translation;
import org.tim.repositories.MessageRepository;
import org.tim.repositories.ProjectRepository;
import org.tim.repositories.TranslationRepository;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageForDeveloperService {

	private final MessageRepository messageRepository;
	private final ProjectRepository projectRepository;
	private final TranslationRepository translationRepository;
	private final ModelMapper mapper = new ModelMapper();

	public List<MessageForDeveloper> getMessagesForDeveloper(Long projectId) {
		List<MessageForDeveloper> messagesForDeveloper = new ArrayList<>();
		Project project = projectRepository.findById(projectId).orElseThrow(() ->
				new NoSuchElementException(String.format("Project with id %s not found", projectId)));
		List<Message> messages = messageRepository.findMessagesByProjectIdAndIsRemovedFalse(projectId);

		for (Message m : messages) {
			MessageForDeveloper mForDeveloper = mapper.map(m, MessageForDeveloper.class);
			mForDeveloper.setProjectId(projectId);
			List<Translation> translations = translationRepository.findAllByMessageAndIsRemovedFalseOrderByLocale(m);

			mForDeveloper.setTranslations(getTranslationsForDeveloper(translations));

			mForDeveloper.setMissingLocales(getMissingLocales(project, translations));

			Map<String, Integer> translationStatuses = new HashMap<>();

			translationStatuses.put("missing", mForDeveloper.getMissingLocales().size());

			Integer incorrectCount = 0;
			for (TranslationForDeveloper t : mForDeveloper.getTranslations()) {
				if (!t.getIsValid() || mForDeveloper.isTranslationOutdated(t)) {
					incorrectCount++;
				}
			}
			translationStatuses.put("incorrect", incorrectCount);
			translationStatuses.put("correct", (translations.size() - incorrectCount));

			mForDeveloper.setTranslationStatuses(translationStatuses);

			messagesForDeveloper.add(mForDeveloper);
		}

		return messagesForDeveloper;
	}

	private List<TranslationForDeveloper> getTranslationsForDeveloper(List<Translation> translations) {
		List<TranslationForDeveloper> translationsForDeveloper = new LinkedList<>();
		for (Translation t : translations) {
			TranslationForDeveloper tForDeveloper = mapper.map(t, TranslationForDeveloper.class);
			tForDeveloper.setMessageId(t.getMessage().getId());
			translationsForDeveloper.add(tForDeveloper);
		}
		return translationsForDeveloper;
	}

	private List<String> getMissingLocales(Project project, List<Translation> translations) {
		List<String> missingLocales = new LinkedList<>();
		Set<LocaleWrapper> targetLocales = new HashSet<>(project.getTargetLocales());
		for (Translation t : translations) {
			Locale locale = t.getLocale();
			for (LocaleWrapper target : targetLocales) {
				if (target.getLocale().equals(locale)) {
					targetLocales.remove(target);
					break;
				}
			}
		}
		for (LocaleWrapper locale : targetLocales) {
			missingLocales.add(locale.getLocale().toString());
		}
		Collections.sort(missingLocales);
		return missingLocales;
	}
}
