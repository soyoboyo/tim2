package org.tim.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tim.DTOs.output.MessageForTranslator;
import org.tim.configurations.Done;
import org.tim.services.MessageForTranslatorService;

import java.util.List;

import static org.tim.utils.Mapping.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + MESSAGE + TRANSLATOR)
public class MessageForTranslatorController {

	private final MessageForTranslatorService messageForTranslatorService;

	@Done
	//@PreAuthorize("hasRole('ROLE_TRANSLATOR')")
	@GetMapping(path = GET_BY_LOCALE)
	public ResponseEntity<List<MessageForTranslator>> getMessagesForTranslator(@PathVariable String projectId, @RequestParam String locale) {
		return ResponseEntity.ok(messageForTranslatorService.getMessagesForTranslator(projectId, locale));
	}

	@Done
	//@PreAuthorize("hasRole('ROLE_TRANSLATOR')")
	@GetMapping(path = GET_BY_PROJECT)
	public ResponseEntity<List<MessageForTranslator>> getMessagesForTranslator(@PathVariable String projectId) {
		return ResponseEntity.ok(messageForTranslatorService.getMessagesForTranslator(projectId));
	}

}
