package org.tim.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static org.tim.constants.Mappings.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + REPORT)
public class ImportController {

    @PostMapping(IMPORT + TRANSLATOR)
    public ResponseEntity<String> importTranslatorCSVReport(MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Empty file");
        }
        return ResponseEntity.ok("success");
    }

    @PostMapping(IMPORT + DEVELOPER)
    public ResponseEntity<String> importDeveloperCSVReport(MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Empty file");
        }

        return ResponseEntity.ok("success");
    }
}
