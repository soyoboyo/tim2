package org.tim.services;


import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tim.DTOs.output.MessageForTranslator;
import org.tim.DTOs.output.TranslationForTranslator;
import org.tim.entities.*;
import org.tim.exceptions.EntityNotFoundException;
import org.tim.repositories.MessageRepository;
import org.tim.repositories.ProjectRepository;
import org.tim.repositories.TranslationRepository;
import org.tim.repositories.TranslationHistoryRepository;
import org.tim.translators.LocaleTranslator;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageForTranslatorService {

	private final MessageRepository messageRepository;
	private final ProjectRepository projectRepository;
	private final TranslationRepository translationRepository;
	private final TranslationHistoryRepository translationHistoryRepository;
	private final MessageVersionService messageVersionService;
	private final LocaleTranslator localeTranslator;

	public List<MessageForTranslator> getMessagesForTranslator(String projectId, String loc) {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new EntityNotFoundException("project"));

		List<Message> messages = messageRepository.findActiveMessagesByProject(projectId);

		Locale locale = localeTranslator.execute(loc);

		List<MessageForTranslator> messagesForTranslator = new ArrayList<>();
		ModelMapper mapper = new ModelMapper();
		for (Message m : messages) {
			MessageForTranslator mForTranslator = mapper.map(m, MessageForTranslator.class);

			Optional<Translation> translation = translationRepository.findTranslationByLocaleAndMessageId(locale, m.getId());
			if (translation.isPresent()) {
				getTranslationForTranslator(mForTranslator, translation.get());
				if (m.isTranslationOutdated(translation.get())) {
					getPreviousMessageContent(mForTranslator);
				}
			} else {
				mForTranslator.setSubstitute(getSubstituteForTranslator(project.getReplaceableLocaleToItsSubstitute(), locale, m.getId()));
			}

			messagesForTranslator.add(mForTranslator);
		}

		messagesForTranslator.sort(Comparator.reverseOrder());

		return messagesForTranslator;
	}

	public List<MessageForTranslator> getMessagesForTranslator(String projectId) {
		List<MessageForTranslator> messagesForTranslator = new ArrayList<>();
		List<Message> messages = messageRepository.findActiveMessagesByProject(projectId);
		ModelMapper mapper = new ModelMapper();
		for (Message m : messages) {
			MessageForTranslator mForTranslator = mapper.map(m, MessageForTranslator.class);
			mForTranslator.setTranslation(null);
			mForTranslator.setSubstitute(null);
			messagesForTranslator.add(mForTranslator);
		}

		messagesForTranslator.sort(Comparator.comparing(MessageForTranslator::getUpdateDate, Comparator.reverseOrder()));

		return messagesForTranslator;
	}

	private void getTranslationForTranslator(MessageForTranslator message, Translation translation) {
		ModelMapper mapper = new ModelMapper();
		TranslationForTranslator tForTranslator = mapper.map(translation, TranslationForTranslator.class);
		message.setTranslation(tForTranslator);
	}

	private String getPreviousMessageContent(MessageForTranslator message) {
		List<TranslationHistory> translationHistories = translationHistoryRepository.findAllByTranslationIdSorted(message.getTranslation().getId());


		Date upperBound = message.getTranslation().getUpdateDate();

		String previousMessageContent = "SOMETHING IS WRONG";
		if (!translationHistories.isEmpty()) {
			Date lowerBound = translationHistories.get(0).getUpdateDate();
			List<MessageHistory> versions = messageVersionService.getMessageVersionsByMessageIdAndUpdateDateBetween(message.getId(), upperBound, lowerBound);
			if (!versions.isEmpty()) {
				previousMessageContent = versions.get(0).getContent();
			}
			message.setPreviousMessageContent(previousMessageContent);
		} else {
			List<MessageHistory> versions = messageVersionService.getMessageVersionsByMessageIdAndUpdateDate(message.getId(), upperBound);
			if (!versions.isEmpty()) {
				previousMessageContent = versions.get(versions.size() - 1).getContent();
			}
			message.setPreviousMessageContent(previousMessageContent);
		}

		return previousMessageContent;
	}

	private TranslationForTranslator getSubstituteForTranslator(Map<Locale, Locale> replaceableLocaleToItsSubstitute, Locale replaceableLocale, String messageId) {
		Translation sub;
		ModelMapper mapper = new ModelMapper();
		do {
			replaceableLocale = replaceableLocaleToItsSubstitute.get(replaceableLocale);
			if (replaceableLocale != null) {
				sub = translationRepository.findTranslationByLocaleAndMessageId(replaceableLocale, messageId).orElse(null);
				if (sub != null && sub.getIsValid())
					return mapper.map(sub, TranslationForTranslator.class);
				continue;
			}
			break;
		} while (true);
		return null;
	}
}
