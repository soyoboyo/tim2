package org.tim.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tim.services.ExportService;
import org.tim.services.ImportService;

import java.io.IOException;

import static org.tim.constants.Mappings.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + REPORT)
public class ReportController {

	private final ExportService exportService;
	private final ImportService importService;

	@GetMapping(value = GENERATE, produces = "text/csv")
	public ResponseEntity<byte[]> generateCsvReportForTranslator(@PathVariable Long id, @RequestParam String[] locales) throws IOException {
		return ResponseEntity.ok(exportService.generateCSVReport(id, locales));
	}

	@PostMapping(IMPORT + TRANSLATOR)
	public ResponseEntity<String> importTranslatorCSVReport(MultipartFile file) throws Exception {
		importService.importTranslatorCSVFile(file);
		return ResponseEntity.ok("success");
	}

	@PostMapping(IMPORT + DEVELOPER)
	public ResponseEntity<String> importDeveloperCSVMessage(MultipartFile file) throws Exception {
		importService.importDeveloperCSVMessage(file);
		return ResponseEntity.ok("success");
	}

	@ExceptionHandler({Exception.class})
	public ResponseEntity<String> csvProcessingError(Exception exception) {
		return ResponseEntity.badRequest().body(exception.getMessage());
	}
}
