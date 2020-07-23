package org.tim.DTOs.output;

import lombok.Data;

import java.util.Locale;

@Data
public class WarningDTO {

	String outdatedTranslationContent;

	Locale substituteLocale;

	String substituteContent;
}
