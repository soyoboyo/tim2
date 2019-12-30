package org.tim.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tim.DTOs.output.MessageForDeveloperResponse;
import org.tim.annotations.Done;
import org.tim.services.MessageForDeveloperService;

import java.util.List;

import static org.tim.utils.Mapping.*;

@RestController
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ROLE_DEVELOPER')")
@RequestMapping(API_VERSION + MESSAGE + DEVELOPER)
public class MessageForDeveloperController {

	private final MessageForDeveloperService messageForDeveloperService;

	@Done
	@GetMapping(GET_BY_PROJECT)
	public ResponseEntity<List<MessageForDeveloperResponse>> getMessagesForDeveloper(@PathVariable String projectId) {
		return ResponseEntity.ok(messageForDeveloperService.getMessagesForDeveloper(projectId));
	}

}
