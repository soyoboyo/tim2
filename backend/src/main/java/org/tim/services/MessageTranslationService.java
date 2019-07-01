package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.tim.DTOs.MessageDTO;
import org.tim.DTOs.output.MessageWithWarningsDTO;
import org.tim.DTOs.output.WarningDTO;
import org.tim.entities.LocaleWrapper;
import org.tim.entities.Message;
import org.tim.entities.Project;
import org.tim.entities.Translation;
import org.tim.repositories.LocaleWrapperRepository;
import org.tim.repositories.MessageRepository;
import org.tim.repositories.ProjectRepository;
import org.tim.repositories.TranslationRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MessageTranslationService {

	private final MessageRepository messageRepository;
	private final ProjectRepository projectRepository;
	private final TranslationRepository translationRepository;
	private final LocaleWrapperRepository localeWrapperRepository;
	private final ModelMapper mapper = new ModelMapper();

	public List<MessageWithWarningsDTO> getMissingTranslationForProject(Long projectId) {
		List<MessageWithWarningsDTO> list = new ArrayList<>();
		Project project = projectRepository.findById(projectId).orElseThrow(NoSuchElementException::new);
		for (LocaleWrapper localeWrapper : project.getTargetLocales())
			list.addAll(getMissingTranslation(projectId, localeWrapper.getLocale().toString()));
		return list;
	}

	public List<MessageWithWarningsDTO> getMissingTranslation(Long projectId, String loc) {
		Locale locale = new Locale(loc);
		Project project = projectRepository.findById(projectId).orElseThrow(NoSuchElementException::new);
		Optional<LocaleWrapper> substituteLocale = getSubstituteLocaleByLocale(project, locale);
		List<Message> messages = getMessagesWithoutValidTranslations(projectId, locale);
		return setWarnings(messages, locale, project, substituteLocale);

	}

	public List<MessageDTO> getMessagesWithTranslationsByProject(Long projectId) {
		List<MessageDTO> messageDTOS = new ArrayList<>();
		List<Message> messages = messageRepository.findMessagesByProjectIdAndIsRemovedFalse(projectId);
		for (Message message : messages) {
			MessageDTO messageDTO = mapper.map(message, MessageDTO.class);
			messageDTO.setProjectId(message.getProject().getId());
			List<Translation> translationDTOS = getTranslationDTOsByMessage(message.getId());
			messageDTO.setTranslations(translationDTOS);
			messageDTOS.add(messageDTO);
		}
		return messageDTOS;
	}

	public List<Translation> getTranslationDTOsByMessage(Long messageId) {
		List<Translation> translationDTOS = new ArrayList<>();
		List<Translation> translations = translationRepository.findTranslationsByMessageIdAndIsValidTrue(messageId);
		for (Translation translation : translations) {
			Translation translationDTO = mapper.map(translation, Translation.class);
			translationDTOS.add(translationDTO);
		}
		return translationDTOS;
	}

	private List<MessageWithWarningsDTO> setWarnings(List<Message> messages, Locale sourceLocale, Project project, Optional<LocaleWrapper> substituteLocale) {

		List<MessageWithWarningsDTO> messageWithWarningsDTOS = new ArrayList<>();
		for (Message m : messages) {
			WarningDTO warningDTO = new WarningDTO();
			Optional<Translation> originTranslation = translationRepository.findTranslationsByLocaleAndMessage(sourceLocale, m);

			if (originTranslation.isPresent() && m.isTranslationOutdated(originTranslation.get()))
				warningDTO.setOutdatedTranslationContent(originTranslation.get().getContent());
			Optional<Translation> substituteTranslation;
			if (substituteLocale.isPresent()) {
				do {
					substituteTranslation = translationRepository.findTranslationsByLocaleAndMessage(substituteLocale.get().getLocale(), m);
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

	private List<Message> getMessagesWithoutValidTranslations(Long projectId, Locale locale) {
		List<Message> messages = messageRepository.findMessagesByProjectIdAndIsRemovedFalse(projectId);
		Map<Long, Message> messageMap = new HashMap<>();

		for (Message m : messages)
			messageMap.put(m.getId(), m);

		List<Translation> translations = translationRepository.findTranslationsByLocaleAndMessage_ProjectIdAndIsValidTrue(locale, projectId);
		for (Translation t : translations) {
			if (messageMap.containsKey(t.getMessage().getId())) {
				Message message = messageMap.get(t.getMessage().getId());
				if (!message.isTranslationOutdated(t)) {
					messages.remove(message);
				}
			}
		}
		return messages;
	}

	private Optional<LocaleWrapper> getSubstituteLocaleByLocale(Project project, Locale sourceLocale) {
		return project.getSubstituteLocale(localeWrapperRepository.findByLocale(sourceLocale));
	}

}
