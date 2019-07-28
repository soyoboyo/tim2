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
//TODO: Do i get locales as strings here or Locale objects?
    @GetMapping(path = GENERATE)
    public ResponseEntity<FileSystemResource> generateExcelReport(@PathVariable Long projectId, @RequestParam String[] locales) {
        return ResponseEntity.ok(new FileSystemResource(reportService.generateCSVReport(projectId, locales)));
    }
}
