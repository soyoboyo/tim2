package org.tim.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class})
public class PredefinedLocalesServiceTestsIT {

    @InjectMocks
    private PredefinedLocalesService predefinedLocalesService;

    @Test
    public void whenGetPredefinedLanguagesThenReturnNotEmptyList() {
		assertFalse(predefinedLocalesService.getPredefinedLanguages().isEmpty());
    }

    @Test
    public void whenGetPredefinedCountriesThenReturnNotEmptyList() {
		assertFalse(predefinedLocalesService.getPredefinedCounties().isEmpty());
    }

    @Test
    public void whenGetPredefinedLanguagesThenReturnElementsCompatibleWithRegex() {
        predefinedLocalesService.getPredefinedLanguages().forEach(language -> {
            assertTrue(language.matches("[a-z]{2} .{0,}|[a-z]{2}"));
        });
    }

    @Test
    public void whenGetPredefinedCountriesThenReturnElementsCompatibleWithRegex() {
        predefinedLocalesService.getPredefinedCounties().forEach(language -> {
            assertTrue(language.matches("[A-Z]{2} .{0,}|[A-Z]{2}"));
        });
    }
}
