package org.tim.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.tim.DTOs.input.CreateTranslationRequest;
import org.tim.DTOs.input.UpdateTranslationRequest;
import org.tim.configurations.Done;
import org.tim.entities.Translation;
import org.tim.entities.TranslationVersion;
import org.tim.services.TranslationService;
import org.tim.services.TranslationVersionService;
import org.tim.validators.RequestsValidator;

import javax.validation.Valid;
import java.util.List;

import static org.tim.utils.Mapping.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + TRANSLATION)
public class TranslationController {

	private final TranslationService translationService;
	private final TranslationVersionService translationVersionService;
	private final RequestsValidator requestsValidator;

	@Done
	//@PreAuthorize("hasRole('ROLE_TRANSLATOR')")
	@PostMapping(CREATE)
	public ResponseEntity<Translation> createTranslation(
			@RequestBody @Valid CreateTranslationRequest translationRequest,
			@RequestParam String messageId,
			BindingResult bindingResult) {

		requestsValidator.execute(bindingResult);
		return ResponseEntity.ok(translationService.createTranslation(translationRequest, messageId));
	}

	@Done
	//@PreAuthorize("hasRole('ROLE_TRANSLATOR')")
	@PostMapping(UPDATE)
	public ResponseEntity<Translation> updateTranslation(
			@RequestBody @Valid UpdateTranslationRequest translationRequest,
			@PathVariable("id") String translationId,
			@RequestParam String messageId,
			BindingResult bindingResult) {

		requestsValidator.execute(bindingResult);
		return ResponseEntity.ok(translationService.updateTranslation(translationRequest, translationId, messageId));
	}

	@Done
	//@PreAuthorize("hasRole('ROLE_TRANSLATOR')")
	@PostMapping(INVALIDATE)
	public ResponseEntity<Translation> invalidateTranslation(
			@PathVariable("id") String translationId,
			@RequestParam String messageId) {

		return ResponseEntity.ok(translationService.invalidateTranslation(translationId, messageId));
	}


	//@PreAuthorize("hasRole('ROLE_TRANSLATOR')")
	@GetMapping(VERSION)
	public ResponseEntity<List<TranslationVersion>> getTranslationVersionsByOriginal(@PathVariable String originalId) {
		return ResponseEntity.ok(translationVersionService.getTranslationVersionsByOriginal(originalId));
	}
}
