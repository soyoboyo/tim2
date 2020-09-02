package org.tim.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tim.services.TranslationExportService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.tim.constants.Mappings.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_DEVELOPER')")
@RequestMapping(API_VERSION + EXPORT_CD)
public class TranslationExportController {

	private final TranslationExportService translationExportService;

	@GetMapping(path = MESSAGE + GET_BY_LOCALE + "/file")
	public ResponseEntity<byte[]> exportAllReadyTranslationsByProjectInZIP(@PathVariable Long projectId, HttpServletResponse response) throws IOException {
		return ResponseEntity.ok(translationExportService.exportAllReadyTranslationsByProjectInZIP(projectId, response));
	}

	@GetMapping(path = MESSAGE + GET_BY_LOCALE)
	public ResponseEntity<byte[]> exportTranslationsForProjectWithGivenLocalesInZIP(@PathVariable Long projectId, @RequestParam String[] locales, HttpServletResponse response) throws IOException {
		return ResponseEntity.ok(translationExportService.exportTranslationsForProjectWithGivenLocalesInZIP(projectId, locales, response));
	}
}
