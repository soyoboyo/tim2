package org.tim.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.tim.configuration.SpringTestsCustomExtension;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.tim.constants.Mappings.*;

class ImportControllerTestIT extends SpringTestsCustomExtension {

    private MockMvc mockMvc;

    private final String BASE_URL = "http://localhost:8081";

    @InjectMocks
    private ImportController importController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(importController).build();
    }

    @Test
    void whenValidFileImportedThenResponseOk() throws Exception {
        //given
        MockMultipartFile sampleFile = new MockMultipartFile("report.csv", "test content".getBytes());

        //when
        mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL + API_VERSION + REPORT + IMPORT)
                .file("file", sampleFile.getBytes()))
                //then
                .andExpect(status().isOk())
                .andExpect(content().string("success"))
                .andDo(print());
    }

    @Test
    void whenEmptyFileImportedThenResponseBadRequest() throws Exception {
        //given
        MockMultipartFile sampleFile = new MockMultipartFile("report.csv", "".getBytes());

        //when
        mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL + API_VERSION + REPORT + IMPORT)
                .file("file", sampleFile.getBytes()))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Empty file"))
                .andDo(print());
    }
}