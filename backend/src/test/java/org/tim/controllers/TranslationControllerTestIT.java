package org.tim.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.LocaleUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.tim.DTOs.TranslationDTO;
import org.tim.configuration.SpringTestsCustomExtension;
import org.tim.entities.Translation;
import org.tim.services.TranslationService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.tim.constants.Mappings.*;

public class TranslationControllerTestIT extends SpringTestsCustomExtension {

	private static ObjectMapper mapper;
	private static Translation expectedTranslation;
	private MockMvc mockMvc;
	private TranslationDTO translationDTO;

	private final String BASE_URL = "http://localhost:8081" + API_VERSION;

	@Mock
	private TranslationService translationService;

	@InjectMocks
	private TranslationController translationController;


	@BeforeAll
	public static void init() {
		mapper = new ObjectMapper();
		expectedTranslation = new Translation();
		expectedTranslation.setContent("Default context");
		expectedTranslation.setLocale(LocaleUtils.toLocale("en_US"));
	}

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(translationController).build();
		translationDTO = new TranslationDTO();
		translationDTO.setMessageId(1L);
		translationDTO.setContent("Default context");
		translationDTO.setLocale("en_US");
	}

	@Test
	@DisplayName("When data validated then allow to create translation.")
	public void whenCreateNewTranslationAndDataCorrectThenSuccess() throws Exception {
		when(translationService.createTranslation(any(), any())).thenReturn(expectedTranslation);
		String jsonRequest = mapper.writeValueAsString(translationDTO);
		mockMvc.perform(post(BASE_URL + TRANSLATION + CREATE)
				.contentType(MediaType.APPLICATION_JSON)
				.param("messageId", "1")
				.content(jsonRequest))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is(expectedTranslation.getContent())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.locale", Matchers.is(expectedTranslation.getLocale().toString())));
	}

	@Test
	@DisplayName("Return validation error when translation content blank.")
	public void whenCreateNewTranslationAndContentBlankThenFailure() throws Exception {
		translationDTO.setContent("");
		String jsonRequest = mapper.writeValueAsString(translationDTO);
		mockMvc.perform(post(BASE_URL + TRANSLATION + CREATE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest)
				.param("messageId", "1"))
				.andExpect(status().isBadRequest())
				.andDo(print());
	}

	@Test
	@DisplayName("Return validation error when translation locale blank.")
	public void whenCreateNewTranslationAndLocaleBlankThenFailure() throws Exception {
		translationDTO.setLocale("");
		String jsonRequest = mapper.writeValueAsString(translationDTO);
		mockMvc.perform(post(BASE_URL + TRANSLATION + CREATE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
				.andExpect(status().isBadRequest())
				.andDo(print());
	}

	@Test
	@DisplayName("Return validation error when message id is null.")
	public void whenCreateNewTranslationAndMessageIdNullThenFailure() throws Exception {
		translationDTO.setMessageId(null);
		String jsonRequest = mapper.writeValueAsString(translationDTO);
		mockMvc.perform(post(BASE_URL + TRANSLATION + CREATE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
				.andExpect(status().isBadRequest())
				.andDo(print());
	}
}
