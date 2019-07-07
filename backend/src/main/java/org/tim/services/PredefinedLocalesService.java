package org.tim.services;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PredefinedLocalesService {

	public TreeSet<String> getPredefinedLanguages() {
		Locale locales[] = Locale.getAvailableLocales();
		TreeSet<String> languages = new TreeSet<>();
		for (int i = 0; i < locales.length; i++) {
			if (!(locales[i].getLanguage().isEmpty() || locales[i].getCountry().isEmpty())) {
				if (locales[i].getLanguage().length() == 2) {
					languages.add(locales[i].getLanguage() + " (" + locales[i].getDisplayLanguage() + ")");
				}
			}
		}
		return languages;
	}

	public TreeSet<String> getPredefinedCounties() {
		Locale locales[] = Locale.getAvailableLocales();
		TreeSet<String> countries = new TreeSet<>();
		for (int i = 0; i < locales.length; i++) {
			if (!(locales[i].getLanguage().isEmpty() || locales[i].getCountry().isEmpty())) {
				if (locales[i].getCountry().length() == 2) {
					countries.add(locales[i].getCountry() + " (" + locales[i].getDisplayCountry() + ")");
				}
			}
		}
		return countries;
	}
}
