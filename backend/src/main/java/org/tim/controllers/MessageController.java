package org.tim.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.tim.DTOs.NewMessageRequest;
import org.tim.configurations.Done;
import org.tim.entities.Message;
import org.tim.entities.MessageVersion;
import org.tim.services.MessageService;
import org.tim.services.MessageVersionService;
import org.tim.validators.RequestsValidator;

import javax.validation.Valid;
import java.util.List;

import static org.tim.utils.Mapping.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + MESSAGE)
public class MessageController {

	private final MessageService messageService;
	private final MessageVersionService messageVersionService;
	private final RequestsValidator requestsValidator;

	@Done
	//@PreAuthorize("hasRole('ROLE_DEVELOPER')")
	@PostMapping(CREATE)
	public ResponseEntity<Message> createMessage(@RequestBody @Valid NewMessageRequest messageRequest, BindingResult bindingResult) {
		requestsValidator.execute(bindingResult);
		return ResponseEntity.ok(messageService.createMessage(messageRequest));
	}

	@Done
	//@PreAuthorize("hasRole('ROLE_DEVELOPER')")
	@PostMapping(UPDATE)
	public ResponseEntity<Message> updateMessage(@RequestBody @Valid NewMessageRequest messageRequest, @PathVariable String id, BindingResult bindingResult) {
		requestsValidator.execute(bindingResult);
		return ResponseEntity.ok(messageService.updateMessage(messageRequest, id));
	}

	@Done
	//@PreAuthorize("hasRole('ROLE_DEVELOPER')")
	@GetMapping(VERSION)
	public ResponseEntity<List<MessageVersion>> getMessageVersionsByOriginalId(@PathVariable String originalId) {
		return ResponseEntity.ok(messageVersionService.getMessageVersionsByOriginalId(originalId));
	}

	@Done
	//@PreAuthorize("hasRole('ROLE_DEVELOPER')")
	@DeleteMapping(value = ARCHIVE)
	public ResponseEntity archiveMessage(@PathVariable String id) {
		return ResponseEntity.ok(messageService.archiveMessage(id));
	}
}
