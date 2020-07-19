package org.tim.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tim.services.CDExportsService;

import java.util.Map;

import static org.tim.constants.Mappings.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + EXPORT_CD)
public class CDExportsController {

	private final CDExportsService cdExportsService;

	@GetMapping(path = MESSAGE + GET_BY_LOCALE + LANG, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, String>> exportAllReadyTranslationsByProjectAndByLocale(@PathVariable Long projectId, @PathVariable String lang) {
		return ResponseEntity.ok(cdExportsService.exportAllReadyTranslationsByProjectAndByLocale(projectId, lang));
	}
}
