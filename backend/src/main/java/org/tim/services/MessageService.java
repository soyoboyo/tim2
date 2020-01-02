package org.tim.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tim.DTOs.input.CreateMessageRequest;
import org.tim.entities.Message;
import org.tim.entities.MessageHistory;
import org.tim.entities.Project;
import org.tim.exceptions.EntityNotFoundException;
import org.tim.repositories.MessageHistoryRepository;
import org.tim.repositories.MessageRepository;
import org.tim.repositories.ProjectRepository;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

	private final MessageRepository messageRepository;
	private final ProjectRepository projectRepository;
	private final MessageHistoryRepository messageHistoryRepository;

	public Message createMessage(CreateMessageRequest messageRequest, String createdBy) {
		getAndValidateProjectById(messageRequest.getProjectId());

		Message message = new Message();
		mapRequestToMessage(messageRequest, message, createdBy);

		return messageRepository.save(message);
	}

	public Message updateMessage(CreateMessageRequest messageRequest, String messageId, String updatedBy) {
		getAndValidateProjectById(messageRequest.getProjectId());
		Message message = getAndValidateMessageById(messageId);

		saveMessageToHistory(message);

		if (message.getIsArchived()) {
			throw new EntityNotFoundException("message");
		}

		mapRequestToMessage(messageRequest, message, updatedBy);
		message.setUpdateDate(new Date());

		return messageRepository.save(message);
	}

	private Project getAndValidateProjectById(String projectId) {
		return projectRepository.findById(projectId)
				.orElseThrow(() -> new EntityNotFoundException("project"));
	}

	private Message mapRequestToMessage(CreateMessageRequest messageRequest, Message message, String createdBy) {
		message.setKey(messageRequest.getKey());
		message.setContent(messageRequest.getContent());
		message.setCreatedBy(createdBy);
		message.setDescription(messageRequest.getDescription());
		message.setProjectId(messageRequest.getProjectId());
		return message;
	}

	public Message archiveMessage(String messageId) {
		Message message = getAndValidateMessageById(messageId);

		saveMessageToHistory(message);
		message.setArchived(true);

		return messageRepository.save(message);
	}

	private Message getAndValidateMessageById(String messageId) {
		return messageRepository.findById(messageId)
				.orElseThrow(() -> new EntityNotFoundException("message"));
	}

	private void saveMessageToHistory(Message message) {
		MessageHistory messageHistory = new MessageHistory();
		messageHistory.setKey(message.getKey());
		messageHistory.setContent(message.getContent());
		messageHistory.setDescription(message.getDescription());
		messageHistory.setUpdateDate(message.getUpdateDate());
		messageHistory.setCreatedBy(message.getCreatedBy());
		messageHistory.setMessageId(message.getId());
		messageHistoryRepository.save(messageHistory);
	}

	public List<MessageHistory> getMessageHistoryByOriginalId(String originalId) {
		return messageHistoryRepository.findAllByMessageIdOrderByUpdateDateDesc(originalId);
	}

}
