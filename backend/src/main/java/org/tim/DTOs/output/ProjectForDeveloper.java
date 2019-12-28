package org.tim.DTOs.output;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.TreeSet;

@Data
@NoArgsConstructor
public class ProjectForDeveloper {

	private String id;

	private String name;

	private String sourceLanguage;

	private String sourceCountry;

	private TreeSet<String> targetLocales;

	private TreeSet<String> availableReplacements;

	private HashMap<String, String> substitutes;
}
