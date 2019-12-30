package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tim.DTOs.output.MessageForDeveloperResponse;
import org.tim.DTOs.output.TranslationForDeveloper;
import org.tim.annotations.Done;
import org.tim.entities.Message;
import org.tim.entities.Project;
import org.tim.entities.Translation;
import org.tim.exceptions.EntityNotFoundException;
import org.tim.repositories.MessageRepository;
import org.tim.repositories.ProjectRepository;
import org.tim.repositories.TranslationRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageForDeveloperService {

	private final MessageRepository messageRepository;
	private final ProjectRepository projectRepository;
	private final TranslationRepository translationRepository;


	@Done
	public List<MessageForDeveloperResponse> getMessagesForDeveloper(String projectId) {
		Project project = getAndValidateProject(projectId);

		List<MessageForDeveloperResponse> messagesForDeveloper = new ArrayList<>();

		List<Message> messages = messageRepository.findActiveMessagesByProject(projectId);

		ModelMapper mapper = new ModelMapper();

		for (Message m : messages) {
			MessageForDeveloperResponse mForDeveloper = mapper.map(m, MessageForDeveloperResponse.class);

			List<Translation> translations = translationRepository.findAllByMessageId(m.getId());

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

		Collections.sort(messagesForDeveloper, Comparator.comparing(m -> m.getUpdateDate().getTime()));
		return messagesForDeveloper;
	}

	private Project getAndValidateProject(String projectId) {
		return projectRepository.findById(projectId)
				.orElseThrow(() -> new EntityNotFoundException("project"));
	}

	private List<TranslationForDeveloper> getTranslationsForDeveloper(List<Translation> translations) {
		ModelMapper mapper = new ModelMapper();
		return translations
				.parallelStream()
				.map(translation -> mapper.map(translation, TranslationForDeveloper.class))
				.collect(Collectors.toList());
	}

	private List<String> getMissingLocales(Project project, List<Translation> translations) {
		List<String> missingLocales = new LinkedList<>();
		Set<Locale> targetLocales = new HashSet<>(project.getTargetLocales());
		for (Translation t : translations) {
			Locale locale = t.getLocale();
			for (Locale target : targetLocales) {
				if (target.equals(locale)) {
					targetLocales.remove(target);
					break;
				}
			}
		}
		for (Locale locale : targetLocales) {
			missingLocales.add(locale.toString());
		}
		Collections.sort(missingLocales);
		return missingLocales;
	}
}
