package org.tim.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tim.DTOs.output.MessageForTranslator;
import org.tim.services.MessageForTranslatorService;

import java.util.List;

import static org.tim.constants.Mappings.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_TRANSLATOR')")
@RequestMapping(API_VERSION + MESSAGE + TRANSLATOR)
public class MessageForTranslatorController {

	private final MessageForTranslatorService messageForTranslatorService;

	@GetMapping(path = GET_BY_LOCALE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MessageForTranslator>> getMessagesForTranslator(@PathVariable Long projectId, @RequestParam String locale) {
		return ResponseEntity.ok(messageForTranslatorService.getMessagesForTranslator(projectId, locale));
	}

	@GetMapping(path = GET_BY_PROJECT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MessageForTranslator>> getMessagesForTranslator(@PathVariable Long projectId) {
		return ResponseEntity.ok(messageForTranslatorService.getMessagesForTranslator(projectId));
	}

}
