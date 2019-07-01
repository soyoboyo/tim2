package org.tim.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tim.services.XlsConversionService;

import java.io.IOException;
import java.io.InputStream;

import static org.tim.utils.Mapping.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(API_VERSION)
@PreAuthorize("hasAnyRole('ROLE_DEVELOPER', 'ROLE_TRANSLATOR')")
public class ExportController {

    private final XlsConversionService xlsConversionService;

    @GetMapping(value = MESSAGE + EXPORT + GET_BY_PROJECT, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody byte[] getXlsForMissingTranslations(@PathVariable Long projectId) throws IOException {
        final InputStream inputXStream = xlsConversionService.getXlsForMessageWithWarnings(projectId);
        return IOUtils.toByteArray(inputXStream);
    }
}
