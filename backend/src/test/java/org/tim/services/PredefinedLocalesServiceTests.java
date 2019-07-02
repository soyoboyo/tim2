package org.tim.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class})
public class PredefinedLocalesServiceTests {

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
            assertTrue(language.matches("[a-zA-Z]{1,}[ ][(][a-zA-ZÚ-\uAB52 -]{1,}[)]"));
        });
    }

    @Test
    public void whenGetPredefinedCountriesThenReturnElementsCompatibleWithRegex() {
        predefinedLocalesService.getPredefinedCounties().forEach(language -> {
            assertTrue(language.matches("[a-zA-Z]{1,}[ ][(][A-Z][a-zA-Z ]{0,}[a-z][)]"));
        });
    }
}
