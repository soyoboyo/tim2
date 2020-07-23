package org.tim.entities;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.tim.constants.TranslationStatus;


@Data
public class ReportDataRow {

	public java.util.Locale Locale;
	public Message message;
	public String translation;
	public TranslationStatus status;
	public String substituteTranslation;
	public java.util.Locale substituteLocale;

	public String getSubstituteTranslation() {
		if (substituteTranslation == null) {
			return StringUtils.EMPTY;
		}
		return substituteTranslation;
	}

	public String getSubstituteLocale() {
		if (substituteLocale == null) {
			return StringUtils.EMPTY;
		}
		return substituteLocale.toString();
	}
}
