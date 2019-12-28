package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.LocaleUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tim.DTOs.output.MessageWithWarningsDTO;
import org.tim.DTOs.output.WarningDTO;
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
public class MessageTranslationService {

	private final MessageRepository messageRepository;
	private final ProjectRepository projectRepository;
	private final TranslationRepository translationRepository;
	private final ModelMapper mapper = new ModelMapper();


	public List<MessageWithWarningsDTO> getMissingTranslation(String projectId, String loc) {
		Locale locale = new Locale(loc);
		Project project = projectRepository.findById(projectId).orElseThrow(NoSuchElementException::new);
		Optional<Locale> substituteLocale = getSubstituteLocaleByLocale(project, locale);
		List<Message> messages = getMessagesWithoutValidTranslations(projectId, locale);
		return setWarnings(messages, locale, project, substituteLocale);

	}

	private List<MessageWithWarningsDTO> setWarnings(List<Message> messages, Locale sourceLocale, Project project, Optional<Locale> substituteLocale) {

		List<MessageWithWarningsDTO> messageWithWarningsDTOS = new ArrayList<>();
		for (Message m : messages) {
			WarningDTO warningDTO = new WarningDTO();
			Optional<Translation> originTranslation = translationRepository.findTranslationsByLocaleAndMessageId(sourceLocale, m.getId());

			if (originTranslation.isPresent() && m.isTranslationOutdated(originTranslation.get()))
				warningDTO.setOutdatedTranslationContent(originTranslation.get().getContent());
			Optional<Translation> substituteTranslation;
			if (substituteLocale.isPresent()) {
				do {
					substituteTranslation = translationRepository.findTranslationsByLocaleAndMessageId(substituteLocale.get(), m.getId());
					substituteLocale = project.getSubstituteLocale(substituteLocale.get());
				} while (substituteTranslation.isEmpty() && substituteLocale.isPresent());
				if (substituteTranslation.isPresent()) {
					warningDTO.setSubstituteLocale(substituteTranslation.get().getLocale());
					warningDTO.setSubstituteContent(substituteTranslation.get().getContent());
				}
			}

			MessageWithWarningsDTO messageDTO = mapper.map(m, MessageWithWarningsDTO.class);
			messageDTO.setProjectId(project.getId());
			messageDTO.setWarnings(warningDTO);
			messageWithWarningsDTOS.add(messageDTO);
		}
		return messageWithWarningsDTOS;
	}

	private List<Message> getMessagesWithoutValidTranslations(String projectId, Locale locale) {
		List<Message> messages = messageRepository.findActiveMessagesByProject(projectId);
		Map<String, Message> messageMap = new HashMap<>();

		for (Message m : messages)
			messageMap.put(m.getId(), m);

		List<Translation> translations = null;//translationRepository.findTranslationsByLocaleAndProjectIdAndIsValidTrue(locale, projectId);
		for (Translation t : translations) {
			if (messageMap.containsKey(t.getMessageId())) {
				Message message = messageMap.get(t.getMessageId());
				if (!message.isTranslationOutdated(t)) {
					messages.remove(message);
				}
			}
		}
		return messages;
	}

	private Optional<Locale> getSubstituteLocaleByLocale(Project project, Locale sourceLocale) {
		//return project.getSubstituteLocale(localeWrapperRepository.findByLocale(sourceLocale));
		return Optional.of(LocaleUtils.toLocale( "pl_PL"));
	}

}
