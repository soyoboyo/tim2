package org.tim.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tim.DTOs.output.MessageForDeveloper;
import org.tim.services.MessageForDeveloperService;

import java.util.List;

import static org.tim.utils.Mapping.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_DEVELOPER')")
@RequestMapping(API_VERSION + MESSAGE + DEVELOPER)
public class MessageForDeveloperController {

	private final MessageForDeveloperService messageForDeveloperService;

	@GetMapping(path = GET_BY_PROJECT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MessageForDeveloper>> getMessagesForDeveloper(@PathVariable Long projectId) {
		return ResponseEntity.ok(messageForDeveloperService.getMessagesForDeveloper(projectId));
	}

}
