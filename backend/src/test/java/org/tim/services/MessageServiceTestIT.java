package org.tim.services;

import org.tim.configuration.SpringTestsCustomExtension;

public class MessageServiceTestIT extends SpringTestsCustomExtension {

//	@Autowired
//	private MessageService messageService;
//
//	private Project project;
//
//	@Autowired
//	private MessageRepository messageRepository;
//
//	@Autowired
//	private MessageVersionService messageVersionService;
//
//	@BeforeEach
//	void setUp() {
//		project = createEmptyGermanToEnglishProject();
//	}
//
//	@Test
//	void whenValidMessageIsGivingThenMessageIsCreated() {
//		//given
//		MessageDTO messageDTO = random(MessageDTO.class);
//		messageDTO.setProjectId(project.getId());
//		//when
//		Message createdMessage = messageService.createMessage(messageDTO);
//		Optional<Message> messageFromDB = messageRepository.findById(createdMessage.getId());
//		//then
//		assertTrue(messageFromDB.isPresent());
//		assertEquals(messageDTO.getContent(), messageFromDB.get().getContent());
//	}
//
//	@Test
//	void whenInvalidMessageIsGivingThenExceptionIsThrown() {
//		//given
//		MessageDTO messageDTO = random(MessageDTO.class);
//		//when
//		//then
//		assertThrows(NoSuchElementException.class, () -> messageService.createMessage(messageDTO));
//	}
//
//	@Test
//	void whenDeletingExistingMessageThenMessageIsDeleted() {
//		//given
//		MessageDTO messageDTO = random(MessageDTO.class);
//		messageDTO.setProjectId(project.getId());
//		Message createdMessage = messageService.createMessage(messageDTO);
//		//when
//		Message message = messageService.archiveMessage(createdMessage.getId());
//		Optional<Message> messageFromDB = messageRepository.findById(message.getId());
//		//then
//		assertTrue(messageFromDB.get().getIsArchived());
//	}
//
//	@Test
//	void whenDeletingNonExistingMessageThenExceptionIsThrown() {
//		//given
//		Long id = 1L;
//		//when
//		//then
//		assertThrows(NoSuchElementException.class, () -> messageService.archiveMessage(id));
//	}
//
//	@Test
//	void whenUpdateExistingNotDeletedMessageThenMessageIsUpdated() {
//		//given
//		MessageDTO messageDTO = random(MessageDTO.class);
//		messageDTO.setProjectId(project.getId());
//		Message createdMessage = messageService.createMessage(messageDTO);
//		messageDTO.setContent("updated_content");
//		//when
//		Message updatedMessage = messageService.updateMessage(messageDTO, createdMessage.getId());
//		Optional<Message> messageFromDB = messageRepository.findById(updatedMessage.getId());
//		//then
//		assertEquals("updated_content", messageFromDB.get().getContent());
//	}
//
//	@Test
//	void whenUpdateExistingDeletedMessageThenExceptionIsThrown() {
//		//given
//		MessageDTO messageDTO = random(MessageDTO.class);
//		messageDTO.setProjectId(project.getId());
//		Message createdMessage = messageService.createMessage(messageDTO);
//		messageDTO.setContent("updated_content");
//		messageService.archiveMessage(createdMessage.getId());
//		//when
//		//then
//		assertThrows(NoSuchElementException.class, () -> messageService.updateMessage(messageDTO, createdMessage.getId()));
//	}
//
//	@Test
//	void whenUpdateNonExistingNotDeletedMessageThenExceptionIsThrown() {
//		//given
//		MessageDTO messageDTO = random(MessageDTO.class);
//		messageDTO.setProjectId(project.getId());
//		Long id = 1L;
//		//when
//		//then
//		assertThrows(NoSuchElementException.class, () -> messageService.updateMessage(messageDTO, id));
//	}
//
//
//	@Test
//	@WithMockUser(username = "prog", password = "prog")
//	void whenMessageIsUpdatedThenMessageVersionIsCreated() {
//		// given
//		Project project = createEmptyGermanToEnglishProject();
//		MessageDTO messageDTO = random(MessageDTO.class);
//		messageDTO.setProjectId(project.getId());
//		messageDTO.setKey("key 0");
//		messageDTO.setContent("content version 0");
//		Message message = messageService.createMessage(messageDTO);
//
//		messageDTO.setKey("key 1");
//		messageDTO.setContent("content version 1");
//		messageService.updateMessage(messageDTO, message.getId());
//		messageDTO.setKey("key 2");
//		messageDTO.setContent("content version 2");
//
//		// when
//		Message updatedMessage = messageService.updateMessage(messageDTO, message.getId());
//		List<MessageVersion> messageVersions = messageVersionService.getMessageVersionsByOriginalId(message.getId());
//		// then
//		assertAll(
//				() -> assertEquals(messageDTO.getContent(), updatedMessage.getContent()),
//				() -> assertFalse(messageVersions.isEmpty()),
//				() -> assertEquals(2, messageVersions.size()),
//				() -> assertEquals("content version 1", messageVersions.get(0).getContent()),
//				() -> assertEquals("content version 0", messageVersions.get(1).getContent())
//		);
//	}
}
