package org.tim.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.tim.DTOs.input.TranslationCreateDTO;
import org.tim.DTOs.input.TranslationUpdateDTO;
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
@PreAuthorize("hasRole('ROLE_TRANSLATOR')")
@RequestMapping(API_VERSION + TRANSLATION)
public class TranslationController {

	private final TranslationService translationService;
	private final TranslationVersionService translationVersionService;

	@PostMapping(CREATE)
	public ResponseEntity<Translation> createTranslation(@RequestBody @Valid TranslationCreateDTO translationCreateDTO,
														 @RequestParam String messageId,
														 BindingResult bindingResult) {
		RequestsValidator.validate(bindingResult);
		return ResponseEntity.ok(translationService.createTranslation(translationCreateDTO, messageId));
	}

	@PostMapping(UPDATE)
	public ResponseEntity<Translation> updateTranslation(@RequestBody @Valid TranslationUpdateDTO translationUpdateDTO,
														 @PathVariable("id") String translationId,
														 @RequestParam String messageId,
														 BindingResult bindingResult) {
		RequestsValidator.validate(bindingResult);
		return ResponseEntity.ok(translationService.updateTranslation(translationUpdateDTO, translationId, messageId));
	}

	@PostMapping(INVALIDATE)
	public ResponseEntity<Translation> invalidateTranslation(@PathVariable("id") String translationId,
															 @RequestParam String messageId) {
		return ResponseEntity.ok(translationService.invalidateTranslation(translationId, messageId));
	}

	@GetMapping(VERSION)
	public ResponseEntity<List<TranslationVersion>> getTranslationVersionsByOriginal(@PathVariable Long originalId) {
		return ResponseEntity.ok(translationVersionService.getTranslationVersionsByOriginal(originalId));
	}
}
