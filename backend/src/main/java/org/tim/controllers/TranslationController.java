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
import org.tim.services.MessageTranslationService;
import org.tim.services.TranslationService;
import org.tim.services.TranslationVersionService;
import org.tim.validators.DTOValidator;

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
	private final MessageTranslationService messageTranslationService;

	@PostMapping(CREATE)
	public ResponseEntity<Translation> createTranslation(@RequestBody @Valid TranslationCreateDTO translationCreateDTO,
														 @RequestParam Long messageId,
														 BindingResult bindingResult) {
		DTOValidator.validate(bindingResult);
		return ResponseEntity.ok(translationService.createTranslation(translationCreateDTO, messageId));
	}

	@PostMapping(UPDATE)
	public ResponseEntity<Translation> updateTranslation(@RequestBody @Valid TranslationUpdateDTO translationUpdateDTO,
														 @PathVariable("id") Long translationId,
														 @RequestParam Long messageId,
														 BindingResult bindingResult) {
		DTOValidator.validate(bindingResult);
		return ResponseEntity.ok(translationService.updateTranslation(translationUpdateDTO, translationId, messageId));
	}

	@PostMapping(INVALIDATE)
	public ResponseEntity<Translation> invalidateTranslation(@PathVariable("id") Long translationId,
															 @RequestParam Long messageId) {
		return ResponseEntity.ok(translationService.invalidateTranslation(translationId, messageId));
	}

	@GetMapping(GET_BY_MESSAGE)
	public ResponseEntity<List<Translation>> getTranslationsByMessage(@PathVariable Long messageId) {
		return ResponseEntity.ok(messageTranslationService.getTranslationDTOsByMessage(messageId));
	}

	@GetMapping(VERSION)
	public ResponseEntity<List<TranslationVersion>> getTranslationVersionsByOriginal(@PathVariable Long originalId) {
		return ResponseEntity.ok(translationVersionService.getTranslationVersionsByOriginal(originalId));
	}
}
