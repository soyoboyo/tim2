package org.tim.services;


import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tim.DTOs.MessageDTO;
import org.tim.entities.Message;
import org.tim.entities.MessageVersion;
import org.tim.entities.Project;
import org.tim.repositories.MessageRepository;
import org.tim.repositories.MessageVersionRepository;
import org.tim.repositories.ProjectRepository;

import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {

	private final MessageRepository messageRepository;
	private final ProjectRepository projectRepository;
	private final MessageVersionRepository messageVersionRepository;
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
