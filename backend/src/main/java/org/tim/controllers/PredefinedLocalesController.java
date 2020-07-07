package org.tim.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tim.services.PredefinedLocalesService;

import java.util.TreeSet;

import static org.tim.constants.Mappings.*;

@RestController
@RequestMapping(API_VERSION + LOCALES)
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_DEVELOPER', 'ROLE_TRANSLATOR')")
public class PredefinedLocalesController {

	private final PredefinedLocalesService predefinedLocalesService;

	@GetMapping(LANGUAGES + GET_ALL)
	public TreeSet<String> getAllPredefinedLanguages() {
		return predefinedLocalesService.getPredefinedLanguages();
	}

	@GetMapping(COUNTRIES + GET_ALL)
	public TreeSet<String> getAllPredefinedCountries() {
		return predefinedLocalesService.getPredefinedCounties();
	}
}
