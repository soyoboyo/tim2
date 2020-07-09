package org.tim.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import org.tim.configuration.SpringTestsCustomExtension;
import org.tim.services.ImportService;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.tim.constants.Mappings.*;

class ImportControllerTestIT extends SpringTestsCustomExtension {

    private MockMvc mockMvc;

    private final String BASE_URL = "http://localhost:8081";

    @InjectMocks
    private ImportController importController;

    @Mock
    private ImportService importService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(importController).build();
    }

    @Test
    void whenValidTranslatorFileImportedThenResponseOk() throws Exception {
        //given
        MockMultipartFile sampleFile = new MockMultipartFile("report.csv", "test content".getBytes());

        //when
        mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL + API_VERSION + REPORT + IMPORT + TRANSLATOR)
                .file("file", sampleFile.getBytes()))
                //then
                .andExpect(status().isOk())
                .andExpect(content().string("success"))
                .andDo(print());
    }

    @Test
    void whenEmptyTranslatorFileImportedThenResponseBadRequest() throws Exception {
        //given
        MockMultipartFile sampleFile = new MockMultipartFile("report.csv", "".getBytes());

        //when
        mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL + API_VERSION + REPORT + IMPORT + TRANSLATOR)
                .file("file", sampleFile.getBytes()))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Empty file"))
                .andDo(print());
    }

    @Test
    void whenValidDeveloperFileImportedThenResponseOk() throws Exception {
        //given
        MockMultipartFile sampleFile = new MockMultipartFile("report.csv", "test content".getBytes());

        //when
        mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL + API_VERSION + REPORT + IMPORT + DEVELOPER)
                .file("file", sampleFile.getBytes()))
                //then
                .andExpect(status().isOk())
                .andExpect(content().string("success"))
                .andDo(print());
    }

    @Test
    void whenEmptyDeveloperFileImportedThenResponseBadRequest() throws Exception {
        //given
        MockMultipartFile sampleFile = new MockMultipartFile("report.csv", "".getBytes());

        //when
        mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL + API_VERSION + REPORT + IMPORT + DEVELOPER)
                .file("file", sampleFile.getBytes()))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Empty file"))
                .andDo(print());
    }

    @Test
    @DisplayName("Check if exception message is returned when processing file from translator")
    void whenParsingTranslatorFileExceptionThenReturnExceptionMessage() throws Exception {
        //given
        MockMultipartFile sampleFile = new MockMultipartFile("file", "asd\nasda".getBytes());

        doThrow(new IOException("Test")).when(importService).importTranslatorCSVFile(any(MultipartFile.class));
        //when
        mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL + API_VERSION + REPORT + IMPORT + TRANSLATOR)
                .file("file", sampleFile.getBytes()))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Test"))
                .andDo(print());
    }

    @Test
    @DisplayName("Check if exception message is returned when processing file from developer")
    void whenParsingDeveloperFileExceptionThenReturnExceptionMessage() throws Exception {
        //given
        MockMultipartFile sampleFile = new MockMultipartFile("file", "asd\nasda".getBytes());

        doThrow(new Exception("Test")).when(importService).importDeveloperCSVMessage(any(MultipartFile.class));
        //when
        mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL + API_VERSION + REPORT + IMPORT + DEVELOPER)
                .file("file", sampleFile.getBytes()))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Test"))
                .andDo(print());
    }
}