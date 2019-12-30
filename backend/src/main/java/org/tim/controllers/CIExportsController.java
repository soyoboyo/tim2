package org.tim.controllers;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim.DTOs.output.LocaleResponse;
import org.tim.annotations.Done;
import org.tim.services.CIExportsService;

import java.util.List;

import static org.tim.utils.Mapping.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + EXPORT_CI)
public class CIExportsController {

	private final CIExportsService ciExportsService;

	@Done
	@ApiOperation(
			value = "Get messages with translations",
			notes = "Returns available messages with translations. When translation not exists" +
					" then return translations from replaceable locales. Returned format is compatible" +
					" with standards Spring properties file.")
	@GetMapping(path = MESSAGE + GET_BY_LOCALE)
	public ResponseEntity<String> exportAllReadyTranslationsByProjectAndByLocale(@PathVariable String projectId, @RequestParam String locale) {
		return ResponseEntity.ok(ciExportsService.exportAllReadyTranslationsByProjectAndByLocale(projectId, locale));
	}

	@Done
	@ApiOperation(
			value = "Get project locales",
			notes = "Returns a list of all locales for the project with the given id.")
	@GetMapping(path = LOCALES + GET_BY_PROJECT)
	public ResponseEntity<List<LocaleResponse>> getAllSupportedLocalesInProject(@PathVariable String projectId) {
		return ResponseEntity.ok(ciExportsService.getAllSupportedLocalesInProject(projectId));
	}

}
