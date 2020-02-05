package org.tim.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim.services.ReportService;
import static org.tim.constants.Mappings.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + REPORT)
public class ReportController {

    private final ReportService reportService;
    @GetMapping(GENERATE)
    public ResponseEntity<FileSystemResource> generateExcelReport(@PathVariable Long id, @RequestParam String[] locales) {
        return ResponseEntity.ok(new FileSystemResource(reportService.generateCSVReport(id, locales)));
    }
}
