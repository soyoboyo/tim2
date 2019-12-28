package org.tim.services;


import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tim.DTOs.NewMessageRequest;
import org.tim.configurations.ToDo;
import org.tim.entities.Message;
import org.tim.entities.MessageVersion;
import org.tim.entities.Project;
import org.tim.exceptions.EntityNotFoundException;
import org.tim.repositories.MessageRepository;
import org.tim.repositories.MessageVersionRepository;
import org.tim.repositories.ProjectRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {

	private final MessageRepository messageRepository;
	private final ProjectRepository projectRepository;
	private final MessageVersionRepository messageVersionRepository;
	private final ModelMapper mapper = new ModelMapper();

	@ToDo("Who created this message")
	public Message createMessage(NewMessageRequest messageRequest) {
		getAndValidateProjectById(messageRequest.getProjectId());

		Message message = new Message();
		mapRequestToMessage(messageRequest, message);

		return messageRepository.save(message);
	}

	@ToDo("Who created this message")
	public Message updateMessage(NewMessageRequest messageRequest, String messageId) {
		getAndValidateProjectById(messageRequest.getProjectId());
		Message message = getAndValidateMessageById(messageId);
		saveMessageVersion(message);

		if (message.isArchived()) {
			throw new EntityNotFoundException("message");
		}

		mapRequestToMessage(messageRequest, message);

		return messageRepository.save(message);
	}

	private Project getAndValidateProjectById(String projectId) {
		return projectRepository.findById(projectId)
				.orElseThrow(() -> new EntityNotFoundException("project"));
	}

	private Message mapRequestToMessage(NewMessageRequest messageRequest, Message message) {
		message.setKey(messageRequest.getKey());
		message.setContent(messageRequest.getContent());
		//message.setCreatedBy("");
		message.setDescription(messageRequest.getDescription());
		message.setProjectId(messageRequest.getProjectId());
		return message;
	}

	public Message archiveMessage(String messageId) {
		Message message =getAndValidateMessageById(messageId);
		saveMessageVersion(message);
		message.setArchived(true);
		return messageRepository.save(message);
	}

	private Message getAndValidateMessageById(String messageId) {
		return messageRepository.findById(messageId)
				.orElseThrow(() -> new EntityNotFoundException("message"));
	}

	private void saveMessageVersion(Message message) {
		MessageVersion messageVersion = mapper.map(message, MessageVersion.class);
		messageVersion.setMessageId(message.getId());
		messageVersionRepository.save(messageVersion);
	}

}
