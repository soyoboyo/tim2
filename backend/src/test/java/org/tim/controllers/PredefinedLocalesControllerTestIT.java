package org.tim.controllers;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.tim.services.PredefinedLocalesService;

import java.util.TreeSet;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.tim.utils.Mapping.*;

@ExtendWith({SpringExtension.class})
public class PredefinedLocalesControllerTestIT {

	private MockMvc mockMvc;
	private static TreeSet<String> expectedPredefinedLanguages;
	private static String[] expectedLanguages;
	private static TreeSet<String> expectedPredefinedCountries;
	private static String[] expectedCountries;

	private final String BASE_URL = "http://localhost:8081";

	@Mock
	private PredefinedLocalesService predefinedLocalesService;

	@InjectMocks
	private PredefinedLocalesController predefinedLocalesController;

	@BeforeAll
	public static void init() {
		expectedPredefinedLanguages = new TreeSet<>();
		expectedPredefinedLanguages.add("pl (Polish)");
		expectedPredefinedLanguages.add("de (German)");
		expectedLanguages = new String[2];
		expectedLanguages[0] = "de (German)";
		expectedLanguages[1] = "pl (Polish)";
		expectedPredefinedCountries = new TreeSet<>();
		expectedPredefinedCountries.add("SG (Singapore)");
		expectedPredefinedCountries.add("ME (Montenegro)");
		expectedCountries = new String[2];
		expectedCountries[0] = "ME (Montenegro)";
		expectedCountries[1] = "SG (Singapore)";
	}

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(predefinedLocalesController).build();
	}

	@Test
	void whenGetAllPredefinedLanguagesThenReturnOkAndListOfLanguages() throws Exception {
		when(predefinedLocalesService.getPredefinedLanguages()).thenReturn(expectedPredefinedLanguages);
		mockMvc.perform(get(BASE_URL + API_VERSION + LOCALES + LANGUAGES + GET_ALL)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0]", Matchers.is(expectedLanguages[0])))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1]", Matchers.is(expectedLanguages[1])));
	}

	@Test
	void whenGetAllPredefinedCountriesThenReturnOkAndListOfCountries() throws Exception {
		when(predefinedLocalesService.getPredefinedCounties()).thenReturn(expectedPredefinedCountries);
		mockMvc.perform(get(BASE_URL + API_VERSION + LOCALES + COUNTRIES + GET_ALL)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0]", Matchers.is(expectedCountries[0])))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1]", Matchers.is(expectedCountries[1])));
	}
}
