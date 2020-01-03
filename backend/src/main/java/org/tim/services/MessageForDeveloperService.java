package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tim.DTOs.output.MessageForDeveloperResponse;
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
public class MessageForDeveloperService {

	private final MessageRepository messageRepository;
	private final ProjectRepository projectRepository;
	private final TranslationRepository translationRepository;

	public List<MessageForDeveloperResponse> getMessagesForDeveloper(String projectId) {
		Project project = getAndValidateProject(projectId);

		List<Message> messages = messageRepository.findActiveMessagesByProject(projectId);
		Set<String> messagesIds = mapMessagesToIds(messages);

		List<Translation> translations = translationRepository.findAllByProjectIdAndMessageIdIn(projectId, messagesIds, Pages.all());
		Map<String, List<Translation>> linkedTranslations = linkTranslationsWithMessageId(translations);

		List<MessageForDeveloperResponse> messagesForDeveloper = messages
				.stream()
				.map(message -> translateMessageForDeveloper(message, project, linkedTranslations.get(message.getId())))
				.sorted(Comparator.comparing(m -> m.getUpdateDate().getTime()))
				.collect(Collectors.toList());

		return messagesForDeveloper;
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

	private MessageForDeveloperResponse translateMessageForDeveloper(Message message, Project project, List<Translation> translations) {
		MessageForDeveloperResponse mForDeveloper = translateMessage(message);

		if (translations == null) translations = Collections.emptyList();
		mForDeveloper.setTranslations(translations);
		mForDeveloper.setMissingLocales(getMissingLocales(project, translations));

		Map<String, Integer> translationStatuses = new HashMap<>();
		translationStatuses.put("missing", mForDeveloper.getMissingLocales().size());
		Integer incorrectCount = countIncorrectTranslations(translations, message);
		translationStatuses.put("incorrect", incorrectCount);
		translationStatuses.put("correct", (translations.size() - incorrectCount));

		mForDeveloper.setTranslationStatuses(translationStatuses);

		return mForDeveloper;
	}

	private MessageForDeveloperResponse translateMessage(Message message) {
		MessageForDeveloperResponse mfdResponse = new MessageForDeveloperResponse();
		mfdResponse.setId(message.getId());
		mfdResponse.setKey(message.getKey());
		mfdResponse.setContent(message.getContent());
		mfdResponse.setDescription(message.getDescription());
		mfdResponse.setUpdateDate(message.getUpdateDate());
		mfdResponse.setProjectId(message.getProjectId());
		return mfdResponse;
	}

	private List<String> getMissingLocales(Project project, List<Translation> translations) {
		Map<Locale, Translation> translationLinkedWithLocale = mapTranslationsWithLocales(translations);
		return project.getTargetLocales()
				.stream()
				.filter(locale -> translationLinkedWithLocale.get(locale) == null)
				.map(locale -> locale.toString())
				.collect(Collectors.toList());
	}

	private Map<Locale, Translation> mapTranslationsWithLocales(List<Translation> translations) {
		return translations
				.stream()
				.collect(Collectors.toMap(Translation::getLocale, t -> t));
	}

	private int countIncorrectTranslations(List<Translation> translations, Message message) {
		return (int) translations
				.stream()
				.filter(translation -> !translation.getIsValid() || message.isTranslationOutdated(translation))
				.count();
	}

}
