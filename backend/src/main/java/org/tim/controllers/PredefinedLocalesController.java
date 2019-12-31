package org.tim.controllers;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tim.services.PredefinedLocalesService;

import java.util.Set;

import static org.tim.utils.Mapping.*;

@RestController
@RequestMapping(API_VERSION + LOCALES)
@RequiredArgsConstructor
public class PredefinedLocalesController {

	private final PredefinedLocalesService predefinedLocalesService;

	@ApiOperation(
			value = "Get predefined languages",
			notes = "Api returns list of predefined languages with short and full name.")
	@PreAuthorize("hasAnyRole('ROLE_DEVELOPER', 'ROLE_TRANSLATOR')")
	@GetMapping(LANGUAGES + GET_ALL)
	public Set<String> getAllPredefinedLanguages() {

		return predefinedLocalesService.getPredefinedLanguages();
	}

	@ApiOperation(
			value = "Get predefined countries",
			notes = "Api returns list of predefined countries with short and full name.")
	@PreAuthorize("hasAnyRole('ROLE_DEVELOPER', 'ROLE_TRANSLATOR')")
	@GetMapping(COUNTRIES + GET_ALL)
	public Set<String> getAllPredefinedCountries() {

		return predefinedLocalesService.getPredefinedCounties();
	}

}
