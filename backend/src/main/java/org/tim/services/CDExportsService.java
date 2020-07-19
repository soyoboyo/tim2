package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.LocaleUtils;
import org.springframework.stereotype.Service;
import org.tim.entities.LocaleWrapper;
import org.tim.entities.Project;
import org.tim.entities.Translation;
import org.tim.exceptions.EntityNotFoundException;
import org.tim.exceptions.ValidationException;
import org.tim.repositories.ProjectRepository;
import org.tim.repositories.TranslationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CDExportsService {

	private final TranslationRepository translationRepository;
	private final ProjectRepository projectRepository;

	public Map<String, String> exportAllReadyTranslationsByProjectAndByLocale(Long projectId, String locale) {
		Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("project"));
		Map<LocaleWrapper, LocaleWrapper> replaceableLocaleToItsSubstitute = project.getReplaceableLocaleToItsSubstitute();

		Locale orderedLocale;
		try {
			orderedLocale = LocaleUtils.toLocale(locale);
		} catch (IllegalArgumentException e) {
			throw new ValidationException("Locale: " + locale + " doesn't exist.");
		}

		List<Translation> translations = translationRepository.findTranslationsByLocaleAndProjectId(orderedLocale, projectId);

		if (translations.isEmpty()) {
			for (Map.Entry<LocaleWrapper, LocaleWrapper> entry : replaceableLocaleToItsSubstitute.entrySet()) {
				if (entry.getKey().getLocale().equals(orderedLocale))
					translations = translationRepository.findTranslationsByLocaleAndProjectId(
							entry.getValue().getLocale(), projectId);
			}
		}

		return translationsToMap(translations);
	}

	private Map<String, String> translationsToMap(List<Translation> translations) {
		Map<String, String> map = new HashMap<>();
		for (Translation translation : translations) {
			map.put(translation.getMessage().getKey(), translation.getContent());
		}

		return map;
	}
}
