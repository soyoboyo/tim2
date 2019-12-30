package org.tim.controllers;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.tim.DTOs.CreateMessageRequest;
import org.tim.entities.Message;
import org.tim.entities.MessageHistory;
import org.tim.services.MessageService;
import org.tim.services.UserService;
import org.tim.validators.RequestsValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static org.tim.utils.Mapping.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + MESSAGE)
public class MessageController {

	private final UserService userService;
	private final MessageService messageService;
	private final RequestsValidator requestsValidator;

	@ApiOperation(
			value = "Create new message",
			notes = "Create new message with given body:\n" +
					"* projectId -> Id of project that created message will belong\n" +
					"* key -> key that will identify message and allows to faster recognize it\n" +
					"* content -> message content\n" +
					"* description -> description that will help translator to translate this message")
	@PreAuthorize("hasRole('ROLE_DEVELOPER')")
	@PostMapping(CREATE)
	public ResponseEntity<Message> createMessage(
			@RequestBody @Valid CreateMessageRequest messageRequest,
			Principal principal,
			BindingResult bindingResult) {

		requestsValidator.execute(bindingResult);
		String username = userService.getUserPrincipal(principal).getUsername();

		return ResponseEntity.ok(messageService.createMessage(messageRequest, username));
	}

	@ApiOperation(
			value = "Update message",
			notes = "Update message with given body. This endpoint work almost the same as create message." +
					" When message is updated the new message that contain previous message is created in history." +
					" ProjectId need to be consistence with old message version or empty.")
	@PreAuthorize("hasRole('ROLE_DEVELOPER')")
	@PostMapping(UPDATE)
	public ResponseEntity<Message> updateMessage(
			@RequestBody @Valid CreateMessageRequest messageRequest,
			@PathVariable String id,
			Principal principal,
			BindingResult bindingResult) {

		requestsValidator.execute(bindingResult);
		String username = userService.getUserPrincipal(principal).getUsername();

		return ResponseEntity.ok(messageService.updateMessage(messageRequest, id, username));
	}

	@ApiOperation(
			value = "Get message history",
			notes = "Get all history to message with given Id")
	@PreAuthorize("hasRole('ROLE_DEVELOPER')")
	@GetMapping(VERSION)
	public ResponseEntity<List<MessageHistory>> getMessageHistoryByOriginalId(@PathVariable String originalId) {

		return ResponseEntity.ok(messageService.getMessageHistoryByOriginalId(originalId));
	}

	@ApiOperation("Archive message")
	@PreAuthorize("hasRole('ROLE_DEVELOPER')")
	@DeleteMapping(value = ARCHIVE)
	public ResponseEntity archiveMessage(@PathVariable String id) {

		return ResponseEntity.ok(messageService.archiveMessage(id));
	}

}
