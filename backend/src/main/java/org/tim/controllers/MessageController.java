package org.tim.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim.DTOs.NewMessageRequest;
import org.tim.entities.Message;
import org.tim.entities.MessageVersion;
import org.tim.services.MessageService;
import org.tim.services.MessageVersionService;

import javax.validation.Valid;
import java.util.List;

import static org.tim.utils.Mapping.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + MESSAGE)
public class MessageController {

	private final MessageService messageService;
	private final MessageVersionService messageVersionService;

	//@PreAuthorize("hasRole('ROLE_DEVELOPER')")
	@PostMapping(value = CREATE)
	public ResponseEntity<Message> createMessage(@RequestBody @Valid NewMessageRequest messageRequest) {
		Message createdMessage = messageService.createMessage(messageRequest);
		return createdMessage != null ? ResponseEntity.ok(createdMessage) : ResponseEntity.badRequest().body(null);
	}

	//@PreAuthorize("hasRole('ROLE_DEVELOPER')")
	@PostMapping(value = UPDATE, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Message> updateMessage(@RequestBody @Valid NewMessageRequest messageRequest, @PathVariable String id) {
		Message updatedMessage = messageService.updateMessage(messageRequest, id);
		return updatedMessage != null ? ResponseEntity.ok(updatedMessage) : ResponseEntity.badRequest().body(null);
	}

	//@PreAuthorize("hasRole('ROLE_DEVELOPER')")
	@GetMapping(VERSION)
	public ResponseEntity<List<MessageVersion>> getMessageVersionsByOriginalId(@PathVariable Long originalId) {
		return ResponseEntity.ok(messageVersionService.getMessageVersionsByOriginalId(originalId));
	}

	//@PreAuthorize("hasRole('ROLE_DEVELOPER')")
	@DeleteMapping(value = ARCHIVE)
	public ResponseEntity archiveMessage(@PathVariable String id) {
		return ResponseEntity.ok(messageService.archiveMessage(id));
	}
}
