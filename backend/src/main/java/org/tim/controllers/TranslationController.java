package org.tim.controllers;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.tim.DTOs.input.CreateTranslationRequest;
import org.tim.DTOs.input.UpdateTranslationRequest;
import org.tim.annotations.Done;
import org.tim.entities.Translation;
import org.tim.entities.TranslationHistory;
import org.tim.services.TranslationService;
import org.tim.services.UserService;
import org.tim.validators.RequestsValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static org.tim.utils.Mapping.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + TRANSLATION)
public class TranslationController {

	private final UserService userService;
	private final TranslationService translationService;
	private final RequestsValidator requestsValidator;

	@ApiOperation(
			value = "Create translation",
			notes = "Add translation to message with given messageId " +
					"by using request body with params:\n" +
					"* content -> message translation\n" +
					"* locale -> locale of translation (need to exists in project target locales).\n" +
					"If translation with this locale already exists then throws error.")
	@PreAuthorize("hasRole('ROLE_TRANSLATOR')")
	@PostMapping(CREATE)
	public ResponseEntity<Translation> createTranslation(
			@RequestBody @Valid CreateTranslationRequest translationRequest,
			@RequestParam String messageId,
			Principal principal,
			BindingResult bindingResult) {

		String username = userService.getUserPrincipal(principal).getUsername();
		requestsValidator.execute(bindingResult);

		return ResponseEntity.ok(translationService.createTranslation(translationRequest, messageId, username));
	}

	@ApiOperation(
			value = "Update translation",
			notes = "Update translation with given translationId and content.")
	@PreAuthorize("hasRole('ROLE_TRANSLATOR')")
	@PostMapping(UPDATE)
	public ResponseEntity<Translation> updateTranslation(
			@RequestBody @Valid UpdateTranslationRequest translationRequest,
			@PathVariable("id") String translationId,
			Principal principal,
			BindingResult bindingResult) {

		String username = userService.getUserPrincipal(principal).getUsername();
		requestsValidator.execute(bindingResult);

		return ResponseEntity.ok(translationService.updateTranslation(translationRequest, translationId, username));
	}

	@Done
	@ApiOperation(
			value = "Invalidate translation",
			notes = "Invalidate translation with given translationId.")
	@PreAuthorize("hasRole('ROLE_TRANSLATOR')")
	@PostMapping(INVALIDATE)
	public ResponseEntity<Translation> invalidateTranslation(
			@PathVariable("id") String translationId,
			Principal principal) {

		String username = userService.getUserPrincipal(principal).getUsername();

		return ResponseEntity.ok(translationService.invalidateTranslation(translationId, username));
	}

	@ApiOperation(
			value = "Get translation history",
			notes = "Return history of change for translation of given originalId")
	@PreAuthorize("hasRole('ROLE_TRANSLATOR')")
	@GetMapping(VERSION)
	public ResponseEntity<List<TranslationHistory>> getTranslationVersionsByOriginal(@PathVariable String originalId) {

		return ResponseEntity.ok(translationService.getTranslationHistoryByParent(originalId));
	}

}
