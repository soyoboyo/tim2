package org.tim.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.tim.services.ImportService;

import static org.tim.constants.Mappings.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + REPORT)
public class ImportController {

    private final ImportService importService;

    @PostMapping(IMPORT + TRANSLATOR)
    public ResponseEntity<String> importTranslatorCSVReport(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Empty file");
        }

        importService.importTranslatorCSVFile(file);
        return ResponseEntity.ok("success");
    }

    @PostMapping(IMPORT + DEVELOPER)
    public ResponseEntity<String> importDeveloperCSVMessage(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Empty file");
        }

        importService.importDeveloperCSVMessage(file);
        return ResponseEntity.ok("success");
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> csvParsingError(Exception exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
