package org.tim.services;

import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

@Service
public class PredefinedLocalesService {

	public Set<String> getPredefinedLanguages() {
		Locale locales[] = Locale.getAvailableLocales();
		Set<String> languages = new TreeSet<>();
		for (int i = 0; i < locales.length; i++) {
			if (!(locales[i].getLanguage().isEmpty() || locales[i].getCountry().isEmpty())) {
				if (locales[i].getLanguage().length() == 2) {
					languages.add(locales[i].getLanguage() + " (" + locales[i].getDisplayLanguage() + ")");
				}
			}
		}
		return languages;
	}

	public Set<String> getPredefinedCounties() {
		Locale locales[] = Locale.getAvailableLocales();
		Set<String> countries = new TreeSet<>();
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
