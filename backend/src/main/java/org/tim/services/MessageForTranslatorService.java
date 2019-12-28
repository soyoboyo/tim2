package org.tim.services;


import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.LocaleUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tim.DTOs.output.MessageForTranslator;
import org.tim.DTOs.output.TranslationForTranslator;
import org.tim.entities.*;
import org.tim.exceptions.ValidationException;
import org.tim.repositories.MessageRepository;
import org.tim.repositories.ProjectRepository;
import org.tim.repositories.TranslationRepository;
import org.tim.repositories.TranslationVersionRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageForTranslatorService {

	private final MessageRepository messageRepository;
	private final ProjectRepository projectRepository;
	private final TranslationRepository translationRepository;
	private final TranslationVersionRepository translationVersionRepository;
	private final MessageVersionService messageVersionService;
	private final ModelMapper mapper = new ModelMapper();

	public List<MessageForTranslator> getMessagesForTranslator(String projectId, String loc) {
		List<MessageForTranslator> messagesForTranslator = new ArrayList<>();
		Project project = projectRepository.findById(projectId).orElseThrow(() ->
				new NoSuchElementException(String.format("Project with id %s not found", projectId)));
		List<Message> messages = messageRepository.findMessagesByProjectIdAndIsArchivedFalse(projectId);

		Map<Locale, Locale> replaceableLocaleToItsSubstitute = new HashMap<>();
		for (Map.Entry<Locale, Locale> pair : project.getReplaceableLocaleToItsSubstitute().entrySet()) {
			replaceableLocaleToItsSubstitute.put(pair.getKey(), pair.getValue());
		}
		Locale locale;
		try {
			locale = LocaleUtils.toLocale(loc);
		} catch (IllegalArgumentException e) {
			throw new ValidationException("Source locale: " + loc + " was given in the wrong format.");
		}

		for (Message m : messages) {
			MessageForTranslator mForTranslator = mapper.map(m, MessageForTranslator.class);
			Translation translation = translationRepository.findTranslationByLocaleAndMessageId(locale, m.getId()).orElseThrow();
			mForTranslator.setTranslation(null);
			if (translation != null) {
				getTranslationForTranslator(mForTranslator, translation);
				if (m.isTranslationOutdated(translation)) {
					getPreviousMessageContent(mForTranslator);
				}
			} else {
				mForTranslator.setSubstitute(getSubstituteForTranslator(replaceableLocaleToItsSubstitute, locale, m.getId()));
			}

			messagesForTranslator.add(mForTranslator);
		}

		messagesForTranslator.sort(Comparator.reverseOrder());

		return messagesForTranslator;
	}

	public List<MessageForTranslator> getMessagesForTranslator(String projectId) {
		List<MessageForTranslator> messagesForTranslator = new ArrayList<>();
		List<Message> messages = messageRepository.findMessagesByProjectIdAndIsArchivedFalse(projectId);

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
		TranslationForTranslator tForTranslator = mapper.map(translation, TranslationForTranslator.class);
		message.setTranslation(tForTranslator);
	}

	private String getPreviousMessageContent(MessageForTranslator message) {
		List<TranslationVersion> translationVersions = translationVersionRepository.findAllByTranslationIdSorted(message.getTranslation().getId());


		LocalDateTime upperBound = message.getTranslation().getUpdateDate();

		String previousMessageContent = "SOMETHING IS WRONG";
		if (!translationVersions.isEmpty()) {
			Date lowerBound = translationVersions.get(0).getUpdateDate();
			List<MessageVersion> versions = messageVersionService.getMessageVersionsByMessageIdAndUpdateDateBetween(message.getId(), upperBound, lowerBound);
			if (!versions.isEmpty()) {
				previousMessageContent = versions.get(0).getContent();
			}
			message.setPreviousMessageContent(previousMessageContent);
		} else {
			List<MessageVersion> versions = messageVersionService.getMessageVersionsByMessageIdAndUpdateDate(message.getId(), upperBound);
			if (!versions.isEmpty()) {
				previousMessageContent = versions.get(versions.size() - 1).getContent();
			}
			message.setPreviousMessageContent(previousMessageContent);
		}

		return previousMessageContent;
	}

	private TranslationForTranslator getSubstituteForTranslator(Map<Locale, Locale> replaceableLocaleToItsSubstitute, Locale replaceableLocale, String messageId) {
		Translation sub;
		do {
			replaceableLocale = replaceableLocaleToItsSubstitute.get(replaceableLocale);
			if (replaceableLocale != null) {
				sub = translationRepository.findTranslationByLocaleAndMessageId(replaceableLocale, messageId).orElseThrow();
				if (sub != null && sub.getIsValid())
					return mapper.map(sub, TranslationForTranslator.class);
				continue;
			}
			break;
		} while (true);
		return null;
	}
}
