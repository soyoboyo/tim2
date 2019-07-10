package org.tim.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tim.DTOs.MessageDTO;
import org.tim.entities.Message;
import org.tim.entities.MessageVersion;
import org.tim.services.MessageService;
import org.tim.services.MessageVersionService;

import javax.validation.Valid;
import java.util.List;

import static org.tim.utils.Mapping.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_DEVELOPER')")
@RequestMapping(API_VERSION + MESSAGE)
public class MessageController {

	private final MessageService messageService;
	private final MessageVersionService messageVersionService;


	@DeleteMapping(value = ARCHIVE)
	public ResponseEntity archiveMessage(@PathVariable Long id) {
		return messageService.archiveMessage(id) != null ? ResponseEntity.ok("") : ResponseEntity.badRequest().body("");
	}

	@PostMapping(value = CREATE, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Message> createMessage(@RequestBody @Valid MessageDTO messageDTO) {
		Message createdMessage = messageService.createMessage(messageDTO);
		return createdMessage != null ? ResponseEntity.ok(createdMessage) : ResponseEntity.badRequest().body(null);
	}

	@PostMapping(value = UPDATE, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Message> updateMessage(@RequestBody @Valid MessageDTO messageDTO, @PathVariable Long id) {
		Message updatedMessage = messageService.updateMessage(messageDTO, id);
		return updatedMessage != null ? ResponseEntity.ok(updatedMessage) : ResponseEntity.badRequest().body(null);
	}

	@GetMapping(VERSION)
	public ResponseEntity<List<MessageVersion>> getMessageVersionsByOriginalId(@PathVariable Long originalId) {
		return ResponseEntity.ok(messageVersionService.getMessageVersionsByOriginalId(originalId));
	}
}
