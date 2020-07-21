package org.tim.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.tim.configuration.SpringTestsCustomExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PredefinedLocalesServiceTestsIT extends SpringTestsCustomExtension {

	@InjectMocks
	private PredefinedLocalesService predefinedLocalesService;

	@Test
	@DisplayName("Assert that languages are returned as not empty list.")
	public void whenGetPredefinedLanguagesThenReturnNotEmptyList() {
		assertFalse(predefinedLocalesService.getPredefinedLanguages().isEmpty());
	}

	@Test
	@DisplayName("Assert that countries are returned as not empty list.")
	public void whenGetPredefinedCountriesThenReturnNotEmptyList() {
		assertFalse(predefinedLocalesService.getPredefinedCounties().isEmpty());
    }

	@Test
	@DisplayName("Assert that languages are returned in correct format.")
	public void whenGetPredefinedLanguagesThenReturnElementsCompatibleWithRegex() {
        predefinedLocalesService.getPredefinedLanguages().forEach(language -> {
            assertTrue(language.matches("[a-z]{2} .{0,}|[a-z]{2}"));
        });
    }

	@Test
	@DisplayName("Assert that countries are returned in correct format.")
	public void whenGetPredefinedCountriesThenReturnElementsCompatibleWithRegex() {
        predefinedLocalesService.getPredefinedCounties().forEach(language -> {
            assertTrue(language.matches("[A-Z]{2} .{0,}|[A-Z]{2}"));
        });
    }
}
