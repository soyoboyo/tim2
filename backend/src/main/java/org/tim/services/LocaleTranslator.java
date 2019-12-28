package org.tim.services;

import org.apache.commons.lang.LocaleUtils;
import org.springframework.stereotype.Service;
import org.tim.exceptions.ValidationException;

import java.util.Locale;

@Service
public class LocaleTranslator {

	public Locale execute(String locale) {
		try {
			return LocaleUtils.toLocale(locale);
		} catch (IllegalArgumentException e) {
			throw new ValidationException("Locale: " + locale + " doesn't exist.");
		}
	}

}
