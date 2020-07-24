package org.tim.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tim.services.ExportService;
import org.tim.services.ImportService;

import javax.activation.MimeType;

import static org.tim.constants.Mappings.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + REPORT)
public class ReportController {

	private final ExportService exportService;
	private final ImportService importService;

	@GetMapping(value = GENERATE, produces = "text/csv")
	public ResponseEntity<FileSystemResource> generateCsvReportForTranslator(@PathVariable Long id, @RequestParam String[] locales) {
		return ResponseEntity.ok(new FileSystemResource(exportService.generateCSVReport(id, locales)));
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
