package org.tim.DTOs.output;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Data
@NoArgsConstructor
public class ProjectForDeveloper {

	private String id;

	private String name;

	private String sourceLanguage;

	private String sourceCountry;

	private Set<String> targetLocales;

	private Set<String> availableReplacements;

	private Map<String, String> substitutes;
}
