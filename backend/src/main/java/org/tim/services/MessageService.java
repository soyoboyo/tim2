package org.tim.services;


import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tim.DTOs.MessageDTO;
import org.tim.entities.*;
import org.tim.repositories.*;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {

	private final ProjectRepository projectRepository;
	private final MessageRepository messageRepository;
	private final MessageVersionRepository messageVersionRepository;
	private final TranslationRepository translationRepository;
	private final TranslationVersionRepository translationVersionRepository;
	private final ModelMapper mapper = new ModelMapper();

	public Message createMessage(MessageDTO messageDTO) {
		Project project = checkIfProjectExists(messageDTO.getProjectId());
		final Message message = mapper.map(messageDTO, Message.class);
		message.setProject(project);
		return messageRepository.save(message);
	}

	public Message updateMessage(MessageDTO messageDTO, Long messageId) {
		Project project = checkIfProjectExists(messageDTO.getProjectId());
		Message message = checkIfMessageExists(messageId, messageDTO.getKey());
		saveMessageVersion(message);
		if (!message.getIsArchived()) {
			message.setContent(messageDTO.getContent());
			message.setKey(messageDTO.getKey());
			message.setDescription(messageDTO.getDescription());
			message.setProject(project);
			return messageRepository.save(message);
		} else {
			throw new NoSuchElementException(String.format("Message with id %s is not found, message key - %s", messageId, messageDTO.getKey()));
		}
	}

	public Message archiveMessage(Long id) {
		Message message = checkIfMessageExists(id, "");
		saveMessageVersion(message);
		message.setIsArchived(true);
		return messageRepository.save(message);
	}

	public void deleteMessageAndTranslations(Long messageId) {
		Message message = checkIfMessageExists(messageId, "");
		List<MessageVersion> allMessageHistory = messageVersionRepository.findAllByMessageId(messageId);
		List<Translation> translations = translationRepository.findAllByMessageId(messageId);
		List<TranslationVersion> allTranslationsHistory = new LinkedList<>();
		for (Translation t : translations) {
			allTranslationsHistory.addAll(translationVersionRepository.findAllByTranslationId(t.getId()));
		}

		translationVersionRepository.deleteAll(allTranslationsHistory);
		translationRepository.deleteAll(translations);
		messageVersionRepository.deleteAll(allMessageHistory);
		messageRepository.delete(message);
	}

	private Message checkIfMessageExists(Long id, String key) {
		return messageRepository.findById(id).orElseThrow(() ->
				new NoSuchElementException(String.format("Message with id %s is not found, message key - %s", id, key)));
	}

	private Project checkIfProjectExists(Long id) {
		return projectRepository.findById(id).orElseThrow(() ->
				new NoSuchElementException(String.format("Project with id %s not found", id)));
	}

	private void saveMessageVersion(Message message) {
		MessageVersion messageVersion = mapper.map(message, MessageVersion.class);
		messageVersion.setMessageId(message.getId());
		messageVersionRepository.save(messageVersion);
	}
}
