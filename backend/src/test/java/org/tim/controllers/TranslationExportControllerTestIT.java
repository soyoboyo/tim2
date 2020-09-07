package org.tim.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.tim.configuration.SpringTestsCustomExtension;
import org.tim.services.TranslationExportService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.tim.constants.Mappings.*;

class TranslationExportControllerTestIT extends SpringTestsCustomExtension {

	private MockMvc mockMvc;

	@InjectMocks
	private TranslationExportController translationExportController;
	@Mock
	private TranslationExportService translationExportService;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(translationExportController).build();
	}

	@Test
	@DisplayName("When exporting all ready translations for project expect zip bytes in response")
	void whenExportingAllReadyTranslationsForProjectThenExpectZipBytesInResponse() throws Exception {
		when(translationExportService.exportAllReadyTranslationsByProjectInZIP(any(), any())).thenReturn(expectedZip());

		mockMvc.perform(get(API_VERSION + EXPORT_CD + MESSAGE + GET_BY_LOCALE + "/file", 1L))
				.andExpect(status().isOk())
				.andExpect(content().bytes(expectedZip()))
				.andDo(print());
	}

	@Test
	@DisplayName("When exporting translations for project with given locales, expect zip bytes in response")
	void whenExportingTranslationsForProjectWithGivenLocalesExpectZipBytesInResponse() throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.put("locales", List.of("locale1", "locale2"));

		when(translationExportService.exportTranslationsForProjectWithGivenLocalesInZIP(any(), any(), any())).thenReturn(expectedZip());

		mockMvc.perform(get(API_VERSION + EXPORT_CD + MESSAGE + GET_BY_LOCALE, 1L)
				.params(params))
				.andExpect(status().isOk())
				.andExpect(content().bytes(expectedZip()))
				.andDo(print());
	}

	private byte[] expectedZip() throws IOException {
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			 ZipOutputStream zos = new ZipOutputStream(byteArrayOutputStream)) {
			zos.putNextEntry(new ZipEntry("file1.json"));
			zos.write("Test1".getBytes());
			zos.closeEntry();
			zos.putNextEntry(new ZipEntry("file2.json"));
			zos.write("Test2".getBytes());
			zos.closeEntry();

			zos.finish();

			return byteArrayOutputStream.toByteArray();
		}
	}
}