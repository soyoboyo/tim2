package org.tim.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.ValidationException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

import static org.tim.utils.UserMessages.*;


@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Document(indexName = "project")
public class Project {

	@Id
	@Setter(AccessLevel.NONE)
	private String id;

	@NotBlank
	@NonNull
	private String name;

	@NotNull
	@NonNull
	private Locale sourceLocale;

	//@Setter(AccessLevel.NONE)
	private Map<Locale, Locale> replaceableLocaleToItsSubstitute = new HashMap<>();

	//@Setter(AccessLevel.NONE)
	private Set<Locale> targetLocales = new HashSet<>();

	public void addTargetLocale(@NotNull List<Locale> locales) {
		targetLocales.addAll(locales);
	}

	public void setReplaceableLocaleToItsSubstitute(Map<Locale, Locale> test) {
		this.replaceableLocaleToItsSubstitute = test;
	}
	public void setTargetLocales(Set<Locale> test) {
		this.targetLocales = test;
	}

	public void removeTargetLocale(@NotNull Locale locale) {
		targetLocales.remove(locale);
		removeReplaceableLocale(locale);
		removeSubstituteLocale(locale);
	}

	public Optional<Locale> getSubstituteLocale(Locale replaceableLocale) {
		return Optional.ofNullable(replaceableLocaleToItsSubstitute.get(replaceableLocale));
	}

	public void removeReplaceableLocale(Locale replaceableLocale) {
		replaceableLocaleToItsSubstitute.remove(replaceableLocale);
	}

	public void removeSubstituteLocale(Locale substituteLocale) {
		replaceableLocaleToItsSubstitute.entrySet().removeIf(e -> e.getValue().equals(substituteLocale));
	}

	public void updateSubstituteLocale(Locale replaceableLocale, Locale substituteLocale) {
		if (!targetLocales.contains(replaceableLocale)) {
			throw new ValidationException(formatMessage(LCL_NOT_IN_TARGET, replaceableLocale.toString()));
		}
		if (!targetLocales.contains(substituteLocale)) {
			throw new ValidationException(formatMessage(LCL_NOT_IN_TARGET, replaceableLocale.toString()));
		}
		if (checkIfGraphHasCycles(replaceableLocale, substituteLocale)) {
			throw new ValidationException(LCL_CYCLES);
		}
		replaceableLocaleToItsSubstitute.put(replaceableLocale, substituteLocale);
	}

	private boolean checkIfGraphHasCycles(Locale child, Locale parent) {
		if (replaceableLocaleToItsSubstitute.get(parent) == null) {
			return false;
		} else if (replaceableLocaleToItsSubstitute.get(parent).equals(child)) {
			return true;
		}
		return checkIfGraphHasCycles(child, replaceableLocaleToItsSubstitute.get(parent));
	}
}
