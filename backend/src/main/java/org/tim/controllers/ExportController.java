package org.tim.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.tim.annotations.Done;
import org.tim.services.XlsConversionService;

import java.io.IOException;
import java.io.InputStream;

import static org.tim.utils.Mapping.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(API_VERSION)
public class ExportController {

	private final XlsConversionService xlsConversionService;

	@Done
	//@PreAuthorize("hasAnyRole('ROLE_DEVELOPER', 'ROLE_TRANSLATOR')")
	@GetMapping(value = MESSAGE + EXPORT + GET_BY_PROJECT, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody
	byte[] getXlsForMissingTranslations(@PathVariable String projectId) throws IOException {
		final InputStream inputXStream = xlsConversionService.getXlsForMessageWithWarnings(projectId);
		return IOUtils.toByteArray(inputXStream);
	}
}
