package org.tim.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim.DTOs.output.LocaleDTO;
import org.tim.services.CIExportsService;

import java.util.List;

import static org.tim.utils.Mapping.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + EXPORT_CI)
public class CIExportsController {

	private final CIExportsService ciExportsService;

	@GetMapping(path = MESSAGE + GET_BY_LOCALE, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> exportAllReadyTranslationsByProjectAndByLocale(@PathVariable Long projectId, @RequestParam String locale) {
		return ResponseEntity.ok(ciExportsService.exportAllReadyTranslationsByProjectAndByLocale(projectId, locale));
	}

	@GetMapping(path = LOCALES + GET_BY_PROJECT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<LocaleDTO>> getAllSupportedLocalesInProject(@PathVariable Long projectId) {
		return ResponseEntity.ok(ciExportsService.getAllSupportedLocalesInProject(projectId));
	}
}
